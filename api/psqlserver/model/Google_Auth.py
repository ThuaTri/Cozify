# google_auth.py

from google.oauth2 import id_token
import datetime
from google.auth.transport import requests
# from fastapi import HTTPException, Query
from config import CLIENT_ID,db_config

import pymssql
import hashlib

class GoogleAuth:
    def __init__(self):
        self.client_id = CLIENT_ID
        self.db_config  = db_config
        self.role_id = 3 # role user
        
        self.username = None
        self.email = None
        self.password = None
        

    def login(self, idinfo):
        email = idinfo.get("email", None)
        
        if email:
            # Check if the email exists in the database
            if self.email_exists_in_database(email):
                print("email checked: ", email)
                return True
        # If idinfo is not provided or the email doesn't exist, go to the register method
        return self.register(idinfo)

    def email_exists_in_database(self, email):
        try:
            with pymssql.connect(**self.db_config) as conn:
                cursor = conn.cursor()
                cursor.execute("SELECT email FROM user WHERE email = %s", (email,))
                result = cursor.fetchone()
                return result is not None
        except Exception as e:
            return False

    def register(self, idinfo):
        print("register")
        if idinfo:
            self.username = idinfo["email"].split("@")[0]
            self.email = idinfo["email"]
            self.password = idinfo["sub"]
            print("password: %s", self.password)
            
            # Encode the password to bytes using UTF-8
            password_bytes = self.password.encode('utf-8')
            print("password_bytes: %s", password_bytes)
            # Hash the password
            hash_password = hashlib.md5(password_bytes).hexdigest()
            print("hash_password: %s", hash_password)

            print("username: ", self.username)
            print("email: ", self.email)
            
            
            try:
                with pymssql.connect(**self.db_config) as conn:
                    cursor = conn.cursor()
                    cursor.execute("""
                        INSERT INTO [user] (username, email, password, role_id)
                        VALUES (%s, %s, %s, %s)
                    """, (self.username, self.email, hash_password, self.role_id))
                    conn.commit()

                return "Registration successful"
            except Exception as e:
                return "Registration failed: " + str(e)
        else:
            return "Registration failed: idinfo is missing"

    def verify_google_id_token(self, token):
        print("verify_google_id_token")
        try:
            idinfo = id_token.verify_oauth2_token(token, requests.Request(), self.client_id)

            
            self.email = idinfo["email"]
            self.password = idinfo["sub"]

            # Check if the aud claim matches the client ID
            if idinfo["aud"] != self.client_id:
                raise ValueError("Invalid aud claim")

            # Convert the 'exp' claim timestamp to a datetime object
            exp_timestamp = idinfo["exp"]
            exp_datetime = datetime.datetime.utcfromtimestamp(exp_timestamp)

            # Check if the exp claim is greater than the current time
            if exp_datetime <= datetime.datetime.utcnow():
                raise ValueError("Expired token")

            # If all checks pass, return email and password
            if self.login(idinfo):

                
                response_data = {
                    "email": self.email,
                    "password": self.password
                }
                
                print("response_data: ", response_data)
                return response_data

        except ValueError:
            # Invalid token
            return False
    
        
        
def main():
    auth = GoogleAuth()

    # Replace 'YOUR_GOOGLE_ID_TOKEN' with an actual Google ID token for testing
    google_id_token = ""
    login_result = auth.verify_google_id_token(google_id_token)
    if login_result:
        print("Login successful")
    else:
        print("Login failed")

if __name__ == '__main__':
    main()
