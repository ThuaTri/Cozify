import g4f

g4f.debug.logging = False # enable logging
g4f.check_version = False # Disable automatic version checking
print(g4f.version) # check version
print(g4f.Provider.Ails.params)  # supported args
food_name = "bánh mì"
# Automatic selection of provider
t= f'As a waiter of a restaurant, generate a description of {food_name} in a non-scientific way. The description should be easy to understand for normal people. The description must be written in Vietnamese with a language that is close to human language as possible. Avoid descriptive and robotic descriptions. The description length must be limited to 1 paragraph and must not exceed 30 words. Do not add recommendations in the last sentence.'
# streamed completion
response = g4f.ChatCompletion.create(
    model="gpt-3.5-turbo",
    messages=[{"role": "user", "content": t}],
)



print(response)