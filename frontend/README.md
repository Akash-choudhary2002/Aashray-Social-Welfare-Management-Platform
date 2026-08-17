# Aashray Frontend (Phase 6)

React + Vite frontend for the Aashray platform. See the root [`README.md`](../README.md#phase-6-addition) for the full folder structure, routing model, and how this fits into the rest of the platform.

## Quick start

```bash
cp .env.example .env   # set VITE_API_BASE_URL if the API Gateway isn't on localhost:8080
npm install
npm run dev             # http://localhost:5173
```

The API Gateway (`:8080`) and the backend service(s) you're testing must already be running — every request from this app goes through the gateway, never directly to a microservice.
