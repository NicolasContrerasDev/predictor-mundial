import pandas as pd
from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import train_test_split
from sklearn.metrics import classification_report
import joblib

# Cargar datos
df = pd.read_csv("matches.csv")

# Features — deben coincidir EXACTAMENTE con el orden en predictor.py
X = df[[
    "elo_local",
    "elo_visita",
    "ranking_local",
    "ranking_visita",
    "goles_favor_local",
    "goles_favor_visita",
    "goles_contra_local",
    "goles_contra_visita",
    "forma_local",
    "forma_visita"
]]

y = df["resultado"]

X_train, X_test, y_train, y_test = train_test_split(
    X, y, test_size=0.2, random_state=42
)

model = RandomForestClassifier(n_estimators=100, random_state=42)
model.fit(X_train, y_train)

y_pred = model.predict(X_test)
print("=== Evaluación del modelo ===")
print(classification_report(
    y_test, y_pred,
    target_names=["Visita gana (0)", "Local gana (1)", "Empate (2)"]
))

joblib.dump(model, "model.pkl")
print("Modelo guardado en model.pkl")