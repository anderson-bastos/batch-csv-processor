import csv
import random
from datetime import datetime, timedelta

# Configuração
filename = "person_data.csv"
num_records = 80000
statuses = ["ACTIVE", "INACTIVE"]

# Função para gerar números de documento (CPF fictício)
def generate_document_number():
    return str(random.randint(10000000000, 99999999999))

# Criando o CSV
with open(filename, mode="w", newline="") as file:
    writer = csv.writer(file)
    writer.writerow(["pep_document_number"])  # Cabeçalho

    for i in range(1, num_records + 1):
        document_number = generate_document_number()
        writer.writerow([document_number])

print(f"Arquivo {filename} gerado com {num_records} registros.")
