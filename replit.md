# Ordriva — Distributed Order & Fulfillment Platform

Ordriva is a frontend-first operations control room for high-volume orders, inventory, warehouses, payments, fulfillment, events, analytics, and system health.

## Run & Operate

- `pnpm --filter @workspace/api-server run dev` — run the API server (port 5000)
- `pnpm run typecheck` — full typecheck across all packages
- `pnpm run build` — typecheck + build all packages
- `pnpm --filter @workspace/api-spec run codegen` — regenerate API hooks and Zod schemas from the OpenAPI spec
- `pnpm --filter @workspace/db run push` — push DB schema changes (dev only)
- Required env: `DATABASE_URL` — Postgres connection string

## Stack

- pnpm workspaces, Node.js 24, TypeScript 5.9
- API: Express 5
- DB: PostgreSQL + Drizzle ORM
- Validation: Zod (`zod/v4`), `drizzle-zod`
- API codegen: Orval (from OpenAPI spec)
- Build: esbuild (CJS bundle)

## Where things live

- `artifacts/ordriva/src/App.tsx` — route map for the complete Phase 1 product surface.
- `artifacts/ordriva/src/components/OrdrivaShell.tsx` — shared responsive shell, navigation, theme, search, page headers, and reusable status/metric primitives.
- `artifacts/ordriva/src/pages/OrdrivaPages.tsx` — operational pages and interactions.
- `artifacts/ordriva/src/data/mockData.ts` — typed mock models and fixtures kept separate from presentation for later API replacement.
- `artifacts/ordriva/src/index.css` — Ordriva theme tokens and global styles.

## Architecture decisions

- Phase 1 is intentionally frontend-only; no fake REST layer or production telemetry was added.
- Mock entities and fixtures are typed and centralized so real REST services can replace them without redesigning the pages.
- The shared shell owns responsive navigation, global search, theme switching, and cross-page primitives.
- Operational states are rendered as explicit status pills and simulator results are labeled as local fixtures/not connected.

## Product

The app provides a responsive operational overview, searchable order queue with timeline detail, inventory and product views, warehouse capacity, payments, fulfillment pipeline, engineering events, analytics, service health, flash-sale simulator, users, and settings.

## User preferences

_Populate as you build — explicit user instructions worth remembering across sessions._

## Gotchas

- The flash-sale simulator is deliberately local/mock and must not be presented as real load-test telemetry until a backend is connected.
- Keep future API work behind the existing mock data/service boundary rather than embedding fetch logic directly into page markup.

## Pointers

- See the `pnpm-workspace` skill for workspace structure, TypeScript setup, and package details
