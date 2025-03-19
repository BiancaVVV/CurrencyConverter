# CurrencyConverter

Diagrama de arhitectură MVVM
Fluxul de date:

MainActivity → Solicită datele către ExchangeRateViewModel<br>
ExchangeRateViewModel → Interoghează ExchangeRateRepository pentru date<br>
ExchangeRateRepository → Face request către API-ul de schimb valutar<br>
API-ul → Răspunde cu un JSON care este transformat în ExchangeRateResponse<br>
ExchangeRateViewModel → Procesează datele și le trimite către MainActivity<br>
MainActivity → Afișează datele în UI<br>
<img width="770" alt="ARCH" src="https://github.com/user-attachments/assets/32e09f78-b8b2-48cf-a8fb-2675dd4f9c8f" />
<br>
Use Case<br>
<img width="576" alt="usecase" src="https://github.com/user-attachments/assets/357cad21-7c5a-477b-bc85-414118642039" />
<br>
Mock Up<br>
<img width="291" alt="mockup" src="https://github.com/user-attachments/assets/3e6b154f-dfab-4adf-bb62-936c169fb1a7" />




