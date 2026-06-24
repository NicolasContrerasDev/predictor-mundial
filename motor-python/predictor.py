from fastapi import FastAPI
from pydantic import BaseModel
import joblib
import numpy as np

app = FastAPI()

model = joblib.load("model.pkl")

class PartidoRequest(BaseModel):
    equipoLocal: str
    equipoVisita: str
    rankingLocal: int
    rankingVisita: int
    eloLocal: int
    eloVisita: int
    golesFavorLocal: float
    golesFavorVisita: float
    golesContraLocal: float
    golesContraVisita: float
    formaLocal: int
    formaVisita: int

@app.post("/predecir")
def predecir_partido(request: PartidoRequest):

    # El orden DEBE coincidir exactamente con las columnas de train_model.py
    X = np.array([[
        request.eloLocal,
        request.eloVisita,
        request.rankingLocal,
        request.rankingVisita,
        request.golesFavorLocal,
        request.golesFavorVisita,
        request.golesContraLocal,
        request.golesContraVisita,
        request.formaLocal,
        request.formaVisita
    ]])

    # predict_proba devuelve [prob_clase_0, prob_clase_1, prob_clase_2]
    # Clases: 0=visita gana, 1=local gana, 2=empate
    probs = model.predict_proba(X)[0]

    # prediccion devuelve directamente el label (0, 1 o 2)
    prediccion = int(model.predict(X)[0])

    return {
        "equipoLocal":        request.equipoLocal,
        "equipoVisita":       request.equipoVisita,
        "probabilidadLocal":  round(float(probs[1]), 3),   # clase 1
        "probabilidadVisita": round(float(probs[0]), 3),   # clase 0
        "probabilidadEmpate": round(float(probs[2]), 3),   # clase 2
        "prediccion":         prediccion
        # 1 = gana local | 0 = gana visita | 2 = empate
    }