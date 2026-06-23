from fastapi import FastAPI
from pydantic import BaseModel
import random

# Inicializamos la API de Python
app = FastAPI()

# Definimos la estructura de datos que Java nos va a enviar
class PartidoData(BaseModel):
    equipoLocal: str
    equipoVisitante: str
    cuotaLocal: float
    cuotaEmpate: float
    cuotaVisitante: float

@app.post("/predecir")
def predecir_resultado(partido: PartidoData):
    # ==========================================
    # AQUÍ EN EL FUTURO INSERTAREMOS EL MODELO 
    # DE MACHINE LEARNING (scikit-learn/pandas).
    # ==========================================
    
    print(f"Recibiendo datos de Java: {partido.equipoLocal} vs {partido.equipoVisitante}")
    
    # LÓGICA SIMULADA DE PREDICCIÓN (Basada en las cuotas):
    # En las apuestas, la cuota más baja es el resultado más probable.
    cuotas = {
        "1": partido.cuotaLocal,   # 1 = Gana Local
        "X": partido.cuotaEmpate,  # X = Empate
        "2": partido.cuotaVisitante # 2 = Gana Visitante
    }
    
    # El modelo elige el resultado con la cuota menor
    prediccion_calculada = min(cuotas, key=cuotas.get)
    
    # Simulamos el porcentaje de confianza que arrojaría un modelo matemático (ej. 75.5%)
    probabilidad_calculada = round(random.uniform(60.0, 95.0), 2)

    # Le respondemos a Java en formato JSON
    return {
        "prediccion": prediccion_calculada,
        "probabilidadExito": probabilidad_calculada
    }