# CurrencyConverter

Diagrama de arhitectură MVVM
Fluxul de date:

MainActivity → Solicită datele către ExchangeRateViewModel<br>
ExchangeRateViewModel → Interoghează ExchangeRateRepository pentru date<br>
ExchangeRateRepository → Face request către API-ul de schimb valutar<br>
API-ul → Răspunde cu un JSON care este transformat în ExchangeRateResponse<br>
ExchangeRateViewModel → Procesează datele și le trimite către MainActivity<br>
MainActivity → Afișează datele în UI<br>

<img width="289" alt="Screenshot 2025-04-30 at 18 15 31" src="https://github.com/user-attachments/assets/395c8014-9c06-42f2-9961-a07d8dc19bc8" />


<img width="289" alt="Screenshot 2025-04-30 at 18 15 50" src="https://github.com/user-attachments/assets/95fe5cfc-6270-40fd-8943-a3ff1f48a014" />



[Presentation 5.pdf](https://github.com/user-attachments/files/19981058/Presentation.5.pdf)
