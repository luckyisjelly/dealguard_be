# API Spec Draft

All protected endpoints require:

```http
Authorization: Bearer <access-token>
```

Common response:

```json
{
  "success": true,
  "data": {},
  "message": null
}
```

Error response:

```json
{
  "success": false,
  "data": null,
  "message": "error message"
}
```

## Auth

- `POST /api/auth/signup`
- `POST /api/auth/login`
- `GET /api/auth/me`

## Product Posts

- `POST /api/product-posts`
- `GET /api/product-posts/{postId}`
- `PATCH /api/product-posts/{postId}`
- `DELETE /api/product-posts/{postId}`

## Conversations

- `POST /api/conversations`
- `GET /api/conversations`
- `GET /api/conversations/{conversationId}`
- `DELETE /api/conversations/{conversationId}`

## Messages

- `POST /api/conversations/{conversationId}/messages`
- `POST /api/conversations/{conversationId}/messages/bulk`
- `POST /api/conversations/{conversationId}/messages/from-transcript`
- `GET /api/conversations/{conversationId}/messages`

## Analysis

- `POST /api/conversations/{conversationId}/analyze`
- `GET /api/conversations/{conversationId}/summary`
- `GET /api/conversations/{conversationId}/alerts`
- `GET /api/conversations/{conversationId}/condition-history`

## Evidence

- `POST /api/conversations/{conversationId}/evidence-packages`
- `GET /api/conversations/{conversationId}/evidence-packages`
- `GET /api/evidence-packages/{evidencePackageId}/pdf`
