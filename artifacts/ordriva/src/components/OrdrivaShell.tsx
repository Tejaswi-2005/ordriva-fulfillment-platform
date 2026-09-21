import { Bell, Box, ChevronDown, Command, Database, Gauge, GitBranch, LayoutDashboard, Menu, Moon, Package, PanelLeft, Search, Server, Settings, ShieldCheck, ShoppingCart, SlidersHorizontal, Sparkles, Sun, Truck, Users, Wallet, Warehouse as WarehouseIcon, X } from 'lucide-react';
import { type ReactNode, useState } from 'react';
import { Link, useLocation } from 'wouter';

const primaryNav = [
  { href: '/', label: 'Overview', icon: LayoutDashboard },
  { href: '/orders', label: 'Orders', icon: ShoppingCart },
  { href: '/inventory', label: 'Inventory', icon: Box },
  { href: '/products', label: 'Products', icon: Package },
  { href: '/warehouses', label: 'Warehouses', icon: WarehouseIcon },
  { href: '/payments', label: 'Payments', icon: Wallet },
  { href: '/fulfillment', label: 'Fulfillment', icon: Truck },
  { href: '/events', label: 'Events', icon: GitBranch },
];
const platformNav = [
  { href: '/analytics', label: 'Analytics', icon: Gauge },
  { href: '/system-health', label: 'System Health', icon: ShieldCheck },
  { href: '/users', label: 'Users', icon: Users },
  { href: '/settings', label: 'Settings', icon: Settings },
];

export function OrdrivaShell({ children }: { children: ReactNode }) {
  const [location] = useLocation();
  const [mobileOpen, setMobileOpen] = useState(false);
  const [dark, setDark] = useState(false);
  const [searchOpen, setSearchOpen] = useState(false);
  const isActive = (href: string) => href === '/' ? location === '/' : location.startsWith(href);
  const navLink = (item: { href: string; label: string; icon: typeof Box }) => {
    const Icon = item.icon;
    return (
      <Link key={item.href} href={item.href} onClick={() => setMobileOpen(false)} data-testid={`link-nav-${item.label.toLowerCase().replaceAll(' ', '-')}`} className={`group flex h-9 items-center gap-3 rounded-md px-3 text-[13px] font-medium transition-all ${isActive(item.href) ? 'bg-[#d9f06b] text-[#17283d] shadow-[3px_3px_0_#0a1523]' : 'text-[#d7ddda] hover:bg-[#283c55] hover:text-white'}`}>
        <Icon size={16} strokeWidth={isActive(item.href) ? 2.5 : 1.8} />
        <span>{item.label}</span>
        {item.label === 'Events' && <span className="ml-auto font-mono text-[10px] text-[#9aa9b5] group-hover:text-white">LIVE</span>}
      </Link>
    );
  };
  return (
    <div className={dark ? 'dark min-h-[100dvh]' : 'min-h-[100dvh]'}>
      <div className="min-h-[100dvh] bg-background text-foreground">
        <aside className={`fixed inset-y-0 left-0 z-40 flex w-[248px] flex-col bg-sidebar px-3 py-4 text-sidebar-foreground transition-transform duration-200 lg:translate-x-0 ${mobileOpen ? 'translate-x-0' : '-translate-x-full'}`}>
          <div className="flex items-center justify-between px-3 pb-7">
            <Link href="/" data-testid="link-brand" className="flex items-center gap-2.5">
              <span className="grid size-8 place-items-center bg-[#d9f06b] text-[#17283d] shadow-[3px_3px_0_#091421]"><Command size={17} strokeWidth={2.8} /></span>
              <span className="font-mono text-[15px] font-bold tracking-[-0.07em] text-[#f4f6ee]">ordriva<span className="text-[#d9f06b]">/</span></span>
            </Link>
            <button type="button" onClick={() => setMobileOpen(false)} data-testid="button-close-sidebar" className="rounded p-1 text-[#93a2ae] hover:bg-[#283c55] lg:hidden"><X size={18} /></button>
          </div>
          <div className="mb-5 rounded-md border border-[#30465d] bg-[#1d324a] px-3 py-2.5">
            <div className="flex items-center justify-between"><span className="font-mono text-[9px] uppercase tracking-[0.18em] text-[#9aa9b5]">Workspace</span><span className="size-1.5 rounded-full bg-[#d9f06b]" /></div>
            <div className="mt-1 text-sm font-semibold text-[#f4f6ee]">Northstar Logistics</div>
            <div className="mt-0.5 font-mono text-[10px] text-[#9aa9b5]">production · us-west-2</div>
          </div>
          <div className="mb-2 px-3 font-mono text-[9px] uppercase tracking-[0.2em] text-[#718392]">Control room</div>
          <nav className="space-y-1">{primaryNav.map(navLink)}</nav>
          <div className="mb-2 mt-7 px-3 font-mono text-[9px] uppercase tracking-[0.2em] text-[#718392]">Platform</div>
          <nav className="space-y-1">{platformNav.map(navLink)}</nav>
          <div className="mt-auto space-y-3">
            <Link href="/flash-sale" data-testid="link-flash-sale" className={`flex items-center gap-3 rounded-md border px-3 py-3 transition-colors ${isActive('/flash-sale') ? 'border-[#d9f06b] bg-[#30465d] text-[#d9f06b]' : 'border-[#3b536b] bg-[#20364e] text-[#f0f3ea] hover:border-[#d9f06b]'}`}>
              <Sparkles size={16} /><span className="text-[12px] font-semibold">Flash Sale Simulator</span><span className="ml-auto size-1.5 rounded-full bg-[#ee786c]" />
            </Link>
            <div className="flex items-center gap-2 border-t border-[#30465d] px-2 pt-4">
              <div className="grid size-8 place-items-center rounded-full bg-[#f0a77a] font-mono text-[11px] font-bold text-[#17283d]">TK</div>
              <div className="min-w-0"><div className="truncate text-xs font-semibold text-[#f4f6ee]">Tejaswi Kunche</div><div className="font-mono text-[9px] text-[#8798a5]">platform engineer</div></div>
              <button type="button" data-testid="button-profile-menu" className="ml-auto text-[#8798a5] hover:text-white"><ChevronDown size={14} /></button>
            </div>
          </div>
        </aside>
        {mobileOpen && <button type="button" aria-label="Close navigation" data-testid="button-sidebar-backdrop" onClick={() => setMobileOpen(false)} className="fixed inset-0 z-30 bg-[#17283d]/40 lg:hidden" />}
        <div className="lg:pl-[248px]">
          <header className="sticky top-0 z-20 flex h-[62px] items-center justify-between border-b border-border bg-background/95 px-4 backdrop-blur-md sm:px-7">
            <div className="flex items-center gap-3">
              <button type="button" onClick={() => setMobileOpen(true)} data-testid="button-open-sidebar" className="rounded-md p-2 hover:bg-muted lg:hidden"><Menu size={19} /></button>
              <button type="button" onClick={() => setSearchOpen(true)} data-testid="button-global-search" className="flex h-9 w-[220px] items-center gap-2 rounded-md border border-border bg-card px-3 text-left text-xs text-muted-foreground transition-colors hover:border-[#aab96c] sm:w-[280px]"><Search size={15} /><span>Search orders, SKUs, events...</span><kbd className="ml-auto hidden rounded border border-border bg-muted px-1.5 py-0.5 font-mono text-[9px] sm:block">⌘ K</kbd></button>
            </div>
            <div className="flex items-center gap-2 sm:gap-4">
              <div className="hidden items-center gap-2 border-r border-border pr-4 sm:flex"><span className="size-1.5 rounded-full bg-[#58b8a5]" /><span className="font-mono text-[10px] uppercase tracking-wider text-muted-foreground">Production</span></div>
              <button type="button" onClick={() => setDark(!dark)} data-testid="button-theme-toggle" className="rounded-md p-2 text-muted-foreground hover:bg-muted hover:text-foreground">{dark ? <Sun size={16} /> : <Moon size={16} />}</button>
              <button type="button" data-testid="button-notifications" className="relative rounded-md p-2 text-muted-foreground hover:bg-muted hover:text-foreground"><Bell size={17} /><span className="absolute right-1.5 top-1.5 size-1.5 rounded-full bg-[#ee786c]" /></button>
              <div className="hidden size-8 place-items-center rounded-full bg-[#f0a77a] font-mono text-[10px] font-bold text-[#17283d] sm:grid">TK</div>
            </div>
          </header>
          <main className="mx-auto max-w-[1600px] px-4 pb-14 pt-6 sm:px-7 lg:px-9">{children}</main>
        </div>
        {searchOpen && <div className="fixed inset-0 z-50 grid place-items-start bg-[#17283d]/35 px-4 pt-[15vh]" onClick={() => setSearchOpen(false)}><div onClick={(event) => event.stopPropagation()} className="w-full max-w-xl overflow-hidden rounded-lg border border-border bg-card shadow-2xl"><div className="flex items-center gap-3 border-b border-border px-4 py-4"><Search size={18} className="text-muted-foreground" /><input autoFocus data-testid="input-global-search" className="flex-1 bg-transparent text-sm outline-none" placeholder="Search orders, products, warehouse codes..." /><kbd className="font-mono text-[10px] text-muted-foreground">ESC</kbd></div><div className="p-4 text-xs text-muted-foreground"><div className="mb-2 font-mono text-[9px] uppercase tracking-widest">Quick access</div><div className="grid gap-1 sm:grid-cols-2">{['ORD-10482 · Maya Chen', 'Mori Desk Lamp · MDL-204', 'Payment Service · degraded', 'Brooklyn · BK-03'].map((item) => <button type="button" key={item} onClick={() => setSearchOpen(false)} data-testid={`button-search-result-${item.slice(0, 4)}`} className="rounded-md px-3 py-2 text-left hover:bg-muted">{item}</button>)}</div></div></div></div>}
      </div>
    </div>
  );
}

export function PageHeader({ eyebrow, title, description, actions }: { eyebrow: string; title: string; description?: string; actions?: ReactNode }) {
  return <div className="mb-7 flex flex-col justify-between gap-4 border-b border-border pb-6 sm:flex-row sm:items-end"><div><div className="mb-2 flex items-center gap-2 font-mono text-[10px] uppercase tracking-[0.18em] text-muted-foreground"><span className="size-1.5 bg-primary" />{eyebrow}</div><h1 className="font-mono text-2xl font-bold tracking-[-0.07em] text-foreground sm:text-[30px]">{title}</h1>{description && <p className="mt-1.5 max-w-2xl text-sm text-muted-foreground">{description}</p>}</div>{actions && <div className="flex items-center gap-2">{actions}</div>}</div>;
}

export function Button({ children, onClick, variant = 'primary', testId, type = 'button' }: { children: ReactNode; onClick?: () => void; variant?: 'primary' | 'outline' | 'quiet'; testId: string; type?: 'button' | 'submit' }) {
  return <button type={type} onClick={onClick} data-testid={testId} className={`inline-flex h-9 items-center justify-center gap-2 rounded-md px-3.5 text-xs font-semibold transition-all active:translate-y-px ${variant === 'primary' ? 'bg-primary text-primary-foreground shadow-[2px_2px_0_hsl(var(--foreground)/.2)] hover:brightness-95' : variant === 'outline' ? 'border border-border bg-card text-foreground hover:border-[#aab96c] hover:bg-muted' : 'text-muted-foreground hover:bg-muted hover:text-foreground'}`}>{children}</button>;
}

export function StatusPill({ value }: { value: string }) {
  const normalized = value.toUpperCase();
  const tone = normalized.includes('FAIL') || normalized.includes('DOWN') || normalized.includes('OUT') || normalized.includes('EXCEPTION') ? 'bad' : normalized.includes('PENDING') || normalized.includes('DEGRADED') || normalized.includes('LOW') || normalized.includes('RETRY') || normalized.includes('MAINTENANCE') ? 'warn' : normalized.includes('PAID') || normalized.includes('HEALTHY') || normalized.includes('STOCK') || normalized.includes('DELIVERED') || normalized.includes('PROCESSED') || normalized.includes('OPERATIONAL') ? 'good' : 'neutral';
  return <span data-testid={`status-${value.toLowerCase().replaceAll(' ', '-')}`} className={`inline-flex items-center gap-1.5 rounded-sm px-2 py-1 font-mono text-[9px] font-bold tracking-[0.08em] ${tone === 'good' ? 'bg-[#dff3df] text-[#27634b] dark:bg-[#214737] dark:text-[#a8dfb0]' : tone === 'warn' ? 'bg-[#fff0c9] text-[#846017] dark:bg-[#4b3b1d] dark:text-[#f3cd74]' : tone === 'bad' ? 'bg-[#ffe0da] text-[#a34338] dark:bg-[#522823] dark:text-[#f5a096]' : 'bg-muted text-muted-foreground'}`}><span className="size-1.5 rounded-full bg-current" />{value.replaceAll('_', ' ')}</span>;
}

export function MetricCard({ label, value, detail, tone = 'default' }: { label: string; value: string; detail: string; tone?: 'default' | 'good' | 'warn' }) {
  return <div data-testid={`metric-${label.toLowerCase().replaceAll(' ', '-')}`} className={`relative overflow-hidden rounded-md border border-border bg-card p-4 ${tone === 'warn' ? 'border-l-2 border-l-[#ee786c]' : tone === 'good' ? 'border-l-2 border-l-[#58b8a5]' : ''}`}><div className="font-mono text-[10px] uppercase tracking-[0.12em] text-muted-foreground">{label}</div><div className="mt-2 font-mono text-2xl font-bold tracking-[-0.06em] text-foreground">{value}</div><div className={`mt-2 text-[11px] ${tone === 'warn' ? 'text-[#b05043]' : tone === 'good' ? 'text-[#32816d]' : 'text-muted-foreground'}`}>{detail}</div></div>;
}