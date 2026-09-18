export type PaymentStatus = 'PAID' | 'PENDING' | 'FAILED' | 'REFUNDED';
export type FulfillmentStatus = 'DELIVERED' | 'IN_TRANSIT' | 'PACKING' | 'PICKING' | 'PENDING';
export type OrderStatus = 'CONFIRMED' | 'PROCESSING' | 'SHIPPED' | 'DELIVERED' | 'EXCEPTION';
export type InventoryStatus = 'IN_STOCK' | 'LOW_STOCK' | 'OUT_OF_STOCK';
export type OperationalStatus = 'OPERATIONAL' | 'DEGRADED' | 'MAINTENANCE';

export interface TimelineEvent { label: string; time: string; complete: boolean; tone?: 'normal' | 'warn'; }
export interface Order { id: string; customer: string; products: string[]; total: number; warehouse: string; paymentStatus: PaymentStatus; fulfillmentStatus: FulfillmentStatus; status: OrderStatus; createdAt: string; timeline: TimelineEvent[]; }
export interface InventoryItem { product: string; sku: string; warehouse: string; availableQuantity: number; reservedQuantity: number; reorderLevel: number; status: InventoryStatus; }
export interface Product { name: string; sku: string; category: string; price: number; stock: number; reorder: number; warehouse: string; active: boolean; }
export interface Warehouse { name: string; code: string; location: string; inventoryCount: number; activeOrders: number; fulfillmentCapacity: number; operationalStatus: OperationalStatus; }
export interface Payment { transactionId: string; orderId: string; amount: number; method: string; status: PaymentStatus; timestamp: string; refundStatus: 'NONE' | 'PENDING' | 'COMPLETE'; }
export interface Fulfillment { id: string; customer: string; stage: 'Confirmed' | 'Picking' | 'Packing' | 'In transit' | 'Delivered'; updated: string; warehouse: string; priority?: boolean; }
export interface EventRecord { type: string; topic: string; timestamp: string; orderId: string; processing: 'PROCESSED' | 'RETRYING' | 'FAILED'; }
export interface ServiceHealth { name: string; status: 'HEALTHY' | 'DEGRADED' | 'DOWN'; latency: string; checked: string; note: string; }

export const orders: Order[] = [
  { id: 'ORD-10482', customer: 'Maya Chen', products: ['AeroPress Clear', 'Filters 100pk'], total: 68.5, warehouse: 'Oakland / OAK-01', paymentStatus: 'PAID', fulfillmentStatus: 'IN_TRANSIT', status: 'SHIPPED', createdAt: 'Today, 09:42', timeline: [{ label: 'Order confirmed', time: '09:42', complete: true }, { label: 'Payment authorized', time: '09:42', complete: true }, { label: 'Picked at OAK-01', time: '10:08', complete: true }, { label: 'In transit', time: '11:31', complete: true }, { label: 'Delivery', time: 'Est. 16:20', complete: false }] },
  { id: 'ORD-10481', customer: 'Jon Bell', products: ['Field Notes 3-pack'], total: 24, warehouse: 'Reno / RNO-02', paymentStatus: 'PAID', fulfillmentStatus: 'PACKING', status: 'PROCESSING', createdAt: 'Today, 09:37', timeline: [{ label: 'Order confirmed', time: '09:37', complete: true }, { label: 'Payment authorized', time: '09:37', complete: true }, { label: 'Picking', time: '09:51', complete: true }, { label: 'Packing', time: 'In progress', complete: false }] },
  { id: 'ORD-10480', customer: 'Priya Raman', products: ['Mori Desk Lamp'], total: 142, warehouse: 'Brooklyn / BK-03', paymentStatus: 'PENDING', fulfillmentStatus: 'PENDING', status: 'EXCEPTION', createdAt: 'Today, 09:29', timeline: [{ label: 'Order confirmed', time: '09:29', complete: true }, { label: 'Payment authorization', time: 'Needs review', complete: false, tone: 'warn' }, { label: 'Reservation', time: 'Waiting', complete: false }] },
  { id: 'ORD-10479', customer: 'Liam Ortega', products: ['Transit Tote', 'Cable Kit'], total: 86.75, warehouse: 'Oakland / OAK-01', paymentStatus: 'PAID', fulfillmentStatus: 'PICKING', status: 'PROCESSING', createdAt: 'Today, 09:22', timeline: [{ label: 'Order confirmed', time: '09:22', complete: true }, { label: 'Payment authorized', time: '09:22', complete: true }, { label: 'Picking', time: 'In progress', complete: false }] },
  { id: 'ORD-10478', customer: 'Noah Williams', products: ['Arc Speaker'], total: 210, warehouse: 'Reno / RNO-02', paymentStatus: 'FAILED', fulfillmentStatus: 'PENDING', status: 'EXCEPTION', createdAt: 'Today, 09:17', timeline: [{ label: 'Order confirmed', time: '09:17', complete: true }, { label: 'Payment authorized', time: 'Declined', complete: false, tone: 'warn' }] },
  { id: 'ORD-10477', customer: 'Sofia Park', products: ['Cobalt Bottle'], total: 31.5, warehouse: 'Brooklyn / BK-03', paymentStatus: 'PAID', fulfillmentStatus: 'DELIVERED', status: 'DELIVERED', createdAt: 'Today, 09:03', timeline: [{ label: 'Order confirmed', time: '09:03', complete: true }, { label: 'Picked', time: '09:18', complete: true }, { label: 'Delivered', time: '10:41', complete: true }] },
  { id: 'ORD-10476', customer: 'Elliot Fraser', products: ['Mori Desk Lamp'], total: 142, warehouse: 'Brooklyn / BK-03', paymentStatus: 'PAID', fulfillmentStatus: 'IN_TRANSIT', status: 'SHIPPED', createdAt: 'Today, 08:55', timeline: [{ label: 'Order confirmed', time: '08:55', complete: true }, { label: 'Picked', time: '09:10', complete: true }, { label: 'In transit', time: '10:02', complete: true }] },
  { id: 'ORD-10475', customer: 'Ari Singh', products: ['AeroPress Clear'], total: 45, warehouse: 'Oakland / OAK-01', paymentStatus: 'REFUNDED', fulfillmentStatus: 'PENDING', status: 'EXCEPTION', createdAt: 'Today, 08:49', timeline: [{ label: 'Order confirmed', time: '08:49', complete: true }, { label: 'Refund issued', time: '09:01', complete: true }] },
];

export const inventory: InventoryItem[] = [
  { product: 'AeroPress Clear', sku: 'APC-001', warehouse: 'Oakland / OAK-01', availableQuantity: 284, reservedQuantity: 31, reorderLevel: 80, status: 'IN_STOCK' },
  { product: 'Mori Desk Lamp', sku: 'MDL-204', warehouse: 'Brooklyn / BK-03', availableQuantity: 12, reservedQuantity: 8, reorderLevel: 24, status: 'LOW_STOCK' },
  { product: 'Field Notes 3-pack', sku: 'FNT-330', warehouse: 'Reno / RNO-02', availableQuantity: 96, reservedQuantity: 12, reorderLevel: 30, status: 'IN_STOCK' },
  { product: 'Arc Speaker', sku: 'ARC-090', warehouse: 'Reno / RNO-02', availableQuantity: 0, reservedQuantity: 4, reorderLevel: 18, status: 'OUT_OF_STOCK' },
  { product: 'Transit Tote', sku: 'TRT-118', warehouse: 'Oakland / OAK-01', availableQuantity: 42, reservedQuantity: 15, reorderLevel: 24, status: 'IN_STOCK' },
  { product: 'Cobalt Bottle', sku: 'CBT-451', warehouse: 'Brooklyn / BK-03', availableQuantity: 18, reservedQuantity: 11, reorderLevel: 20, status: 'LOW_STOCK' },
  { product: 'Cable Kit', sku: 'CBK-032', warehouse: 'Oakland / OAK-01', availableQuantity: 66, reservedQuantity: 9, reorderLevel: 25, status: 'IN_STOCK' },
];

export const products: Product[] = [
  { name: 'AeroPress Clear', sku: 'APC-001', category: 'Kitchen', price: 45, stock: 284, reorder: 80, warehouse: 'Oakland / OAK-01', active: true },
  { name: 'Mori Desk Lamp', sku: 'MDL-204', category: 'Home', price: 142, stock: 12, reorder: 24, warehouse: 'Brooklyn / BK-03', active: true },
  { name: 'Field Notes 3-pack', sku: 'FNT-330', category: 'Stationery', price: 24, stock: 96, reorder: 30, warehouse: 'Reno / RNO-02', active: true },
  { name: 'Arc Speaker', sku: 'ARC-090', category: 'Audio', price: 210, stock: 0, reorder: 18, warehouse: 'Reno / RNO-02', active: true },
  { name: 'Transit Tote', sku: 'TRT-118', category: 'Travel', price: 68, stock: 42, reorder: 24, warehouse: 'Oakland / OAK-01', active: true },
  { name: 'Cobalt Bottle', sku: 'CBT-451', category: 'Travel', price: 31.5, stock: 18, reorder: 20, warehouse: 'Brooklyn / BK-03', active: false },
];

export const warehouses: Warehouse[] = [
  { name: 'Oakland', code: 'OAK-01', location: 'Oakland, CA · Pacific', inventoryCount: 14382, activeOrders: 218, fulfillmentCapacity: 78, operationalStatus: 'OPERATIONAL' },
  { name: 'Reno', code: 'RNO-02', location: 'Reno, NV · Mountain', inventoryCount: 9820, activeOrders: 143, fulfillmentCapacity: 64, operationalStatus: 'OPERATIONAL' },
  { name: 'Brooklyn', code: 'BK-03', location: 'Brooklyn, NY · Eastern', inventoryCount: 7310, activeOrders: 176, fulfillmentCapacity: 91, operationalStatus: 'DEGRADED' },
];

export const payments: Payment[] = [
  { transactionId: 'txn_8c1a94', orderId: 'ORD-10482', amount: 68.5, method: 'Visa •• 4242', status: 'PAID', timestamp: 'Today, 09:42:03', refundStatus: 'NONE' },
  { transactionId: 'txn_25bf10', orderId: 'ORD-10481', amount: 24, method: 'Apple Pay', status: 'PAID', timestamp: 'Today, 09:37:12', refundStatus: 'NONE' },
  { transactionId: 'txn_99ea21', orderId: 'ORD-10480', amount: 142, method: 'Visa •• 0188', status: 'PENDING', timestamp: 'Today, 09:29:45', refundStatus: 'NONE' },
  { transactionId: 'txn_4d771c', orderId: 'ORD-10478', amount: 210, method: 'Amex •• 1006', status: 'FAILED', timestamp: 'Today, 09:17:28', refundStatus: 'NONE' },
  { transactionId: 'txn_31ca80', orderId: 'ORD-10477', amount: 31.5, method: 'Visa •• 4242', status: 'PAID', timestamp: 'Today, 09:03:09', refundStatus: 'NONE' },
  { transactionId: 'txn_11f4a1', orderId: 'ORD-10475', amount: 45, method: 'PayPal', status: 'REFUNDED', timestamp: 'Today, 08:49:51', refundStatus: 'COMPLETE' },
];

export const fulfillment: Fulfillment[] = [
  { id: 'ORD-10482', customer: 'Maya Chen', stage: 'In transit', updated: '11:31', warehouse: 'Oakland', priority: true },
  { id: 'ORD-10481', customer: 'Jon Bell', stage: 'Packing', updated: '10:18', warehouse: 'Reno' },
  { id: 'ORD-10479', customer: 'Liam Ortega', stage: 'Picking', updated: '10:04', warehouse: 'Oakland' },
  { id: 'ORD-10477', customer: 'Sofia Park', stage: 'Delivered', updated: '10:41', warehouse: 'Brooklyn' },
  { id: 'ORD-10476', customer: 'Elliot Fraser', stage: 'In transit', updated: '10:02', warehouse: 'Brooklyn', priority: true },
  { id: 'ORD-10473', customer: 'Camille Hart', stage: 'Confirmed', updated: '09:58', warehouse: 'Reno' },
  { id: 'ORD-10470', customer: 'Theo Grant', stage: 'Delivered', updated: '09:41', warehouse: 'Oakland' },
];

export const events: EventRecord[] = [
  { type: 'OrderConfirmed', topic: 'ordriva.orders.v1', timestamp: '11:42:08.092', orderId: 'ORD-10482', processing: 'PROCESSED' },
  { type: 'InventoryReserved', topic: 'ordriva.inventory.v1', timestamp: '11:41:52.801', orderId: 'ORD-10482', processing: 'PROCESSED' },
  { type: 'PaymentAuthorizationFailed', topic: 'ordriva.payments.v1', timestamp: '11:39:15.432', orderId: 'ORD-10480', processing: 'RETRYING' },
  { type: 'PickListCreated', topic: 'ordriva.fulfillment.v1', timestamp: '11:38:44.110', orderId: 'ORD-10481', processing: 'PROCESSED' },
  { type: 'OrderCreated', topic: 'ordriva.orders.v1', timestamp: '11:37:18.004', orderId: 'ORD-10481', processing: 'PROCESSED' },
  { type: 'ShipmentDispatched', topic: 'ordriva.fulfillment.v1', timestamp: '11:31:03.778', orderId: 'ORD-10482', processing: 'PROCESSED' },
  { type: 'NotificationQueued', topic: 'ordriva.notifications.v1', timestamp: '11:30:55.009', orderId: 'ORD-10482', processing: 'FAILED' },
];

export const serviceHealth: ServiceHealth[] = [
  { name: 'API Gateway', status: 'HEALTHY', latency: '42 ms', checked: '12 sec ago', note: 'All routes responding' },
  { name: 'Order Service', status: 'HEALTHY', latency: '68 ms', checked: '12 sec ago', note: 'Write path nominal' },
  { name: 'Inventory Service', status: 'HEALTHY', latency: '51 ms', checked: '12 sec ago', note: 'Reservations flowing' },
  { name: 'Payment Service', status: 'DEGRADED', latency: '420 ms', checked: '12 sec ago', note: 'Elevated provider latency' },
  { name: 'Fulfillment Service', status: 'HEALTHY', latency: '84 ms', checked: '12 sec ago', note: 'Queue depth stable' },
  { name: 'Notification Service', status: 'DOWN', latency: '—', checked: '14 sec ago', note: 'Provider connection lost' },
];

export const throughput = [{ time: '06:00', orders: 80, fulfilled: 62 }, { time: '08:00', orders: 142, fulfilled: 118 }, { time: '10:00', orders: 228, fulfilled: 201 }, { time: '12:00', orders: 314, fulfilled: 277 }, { time: '14:00', orders: 268, fulfilled: 245 }, { time: '16:00', orders: 340, fulfilled: 308 }, { time: '18:00', orders: 298, fulfilled: 284 }];
export const activity = [{ text: 'Payment retry succeeded', detail: 'ORD-10474 · Payment Service', time: '2m ago', tone: 'good' }, { text: 'Inventory threshold crossed', detail: 'Mori Desk Lamp · BK-03', time: '6m ago', tone: 'warn' }, { text: 'Shipment dispatched', detail: 'ORD-10482 · OAK-01', time: '11m ago', tone: 'good' }, { text: 'Notification delivery failed', detail: 'ORD-10482 · Notification Service', time: '14m ago', tone: 'bad' }, { text: 'Warehouse capacity updated', detail: 'Brooklyn · 91% utilized', time: '22m ago', tone: 'neutral' }];