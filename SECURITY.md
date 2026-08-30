# Security notes

## Secrets
Real credentials and JWT signing keys must be provided through environment variables or a production secret manager. Never commit `.env` or real values.

## Production recommendations
- Terminate TLS at a trusted ingress/load balancer and enforce HTTPS end-to-end where required.
- Replace demo in-memory users with an enterprise Identity Provider (OIDC/OAuth 2.0).
- For an ASVS Level 2 target, require MFA or document the rationale and compensating controls.
- Add centralized rate limiting / anti-automation at gateway or identity-provider level.
- Centralize security event logging without logging passwords, JWTs, or secret keys.
- Use key rotation and, in distributed systems, prefer asymmetric signing (e.g. RS256/ES256) so verifiers do not need the signing private key.
