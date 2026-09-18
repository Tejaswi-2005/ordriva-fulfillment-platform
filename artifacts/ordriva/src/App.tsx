import { type ReactNode } from 'react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ErrorBoundary } from '@/components/error-boundary';
import { OrdrivaShell } from '@/components/OrdrivaShell';
import { Toaster } from '@/components/ui/toaster';
import { TooltipProvider } from '@/components/ui/tooltip';
import NotFound from '@/pages/not-found';
import { AnalyticsPage, EventsPage, FlashSalePage, FulfillmentPage, InventoryPage, OrdersPage, OverviewPage, PaymentsPage, ProductsPage, SettingsPage, SystemHealthPage, UsersPage, WarehousesPage } from '@/pages/OrdrivaPages';
import {
  Route,
  Switch,
  useLocation,
  Router as WouterRouter,
} from 'wouter';

const queryClient = new QueryClient();

function Router() {
  return (
    // Keep a shared shell (sidebar, navbar) outside the boundary so it
    // survives a page crash.
    <RoutedErrorBoundary>
      <OrdrivaShell>
        <Switch>
          <Route path="/" component={OverviewPage} />
          <Route path="/orders" component={OrdersPage} />
          <Route path="/inventory" component={InventoryPage} />
          <Route path="/products" component={ProductsPage} />
          <Route path="/warehouses" component={WarehousesPage} />
          <Route path="/payments" component={PaymentsPage} />
          <Route path="/fulfillment" component={FulfillmentPage} />
          <Route path="/events" component={EventsPage} />
          <Route path="/analytics" component={AnalyticsPage} />
          <Route path="/system-health" component={SystemHealthPage} />
          <Route path="/flash-sale" component={FlashSalePage} />
          <Route path="/users" component={UsersPage} />
          <Route path="/settings" component={SettingsPage} />
          <Route component={NotFound} />
        </Switch>
      </OrdrivaShell>
    </RoutedErrorBoundary>
  );
}

function RoutedErrorBoundary({ children }: { children: ReactNode }) {
  const [location] = useLocation();
  return <ErrorBoundary resetKey={location}>{children}</ErrorBoundary>;
}

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <TooltipProvider>
        <WouterRouter base={import.meta.env.BASE_URL.replace(/\/$/, '')}>
          <Router />
        </WouterRouter>
        <Toaster />
      </TooltipProvider>
    </QueryClientProvider>
  );
}

export default App;
