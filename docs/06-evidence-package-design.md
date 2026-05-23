# Evidence Package Design

PDF evidence packages are generated from stored records and analysis results.

## Sections

1. Title page
2. Product post snapshot
3. Final extracted trade condition summary
4. Listing-chat mismatch alerts
5. Condition change alerts
6. Missing condition alerts
7. Ambiguous expression alerts
8. Risky payment alerts
9. Original chat message timeline
10. Generated timestamp
11. SHA-256 hash
12. Disclaimer

Disclaimer:

```text
This document is a structured reference based on transaction records and does not replace legal judgment.
```

## Storage

Generated PDFs are stored under a local `storage/evidence` directory for development. The metadata is stored in the database.

Production can replace this with S3 or another object storage service.
