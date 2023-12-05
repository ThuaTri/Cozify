
import pandas as pd 
df = pd.DataFrame({'Click_Id':['A','B', 'C','D','E'], 'Count':[100,200,300,400,250]})
print("before rename")
print(df.columns)
dt = df.rename(columns={'Count':'Click_Count'})
print("after rename")
print(df.columns)
print("dt column")
print(dt.columns)
