# app.py
from fastapi import FastAPI, Depends, Request, Query
from model.Google_Auth import GoogleAuth
from fastapi.middleware.cors import CORSMiddleware
from starlette.staticfiles import StaticFiles
from fastapi.responses import HTMLResponse
import uvicorn
app = FastAPI()
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # Thay đổi thành nguồn của ứng dụng JavaScript của bạn
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.mount("/model", StaticFiles(directory="model"), name="model")

@app.get('/get_credential/{token_info}')
async def get_credential(token_info: str):
    google_auth = GoogleAuth()
    return google_auth.verify_google_id_token(token_info)


if __name__ == '__main__': 
    uvicorn.run('app:app', port=8001, host='0.0.0.0')
