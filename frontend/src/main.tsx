import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter, Navigate, Route, Routes } from 'react-router'
import Login from './pages/Login'
import TermOfService from './pages/TermOfService'
import PrivacyPolicy from './pages/PrivacyPolicy'
import TodayStaff from './pages/TodayStaff'
import TodayTicket from './pages/TodayTicket'
import Ticket from './pages/Ticket'
import Customer from './pages/Customer'
import Settings from './pages/Settings'
import NotFound from './pages/NotFound'
import Performance from './pages/Performance'
import Report from './pages/Report'
import Layout from './components/layout/Layout'
createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/term-of-service" element={<TermOfService />} />
        <Route path="/privacy-policy" element={<PrivacyPolicy />} />


        <Route element={<Layout />} >
          <Route path="/" element={<Navigate to="/today-staff" />} />
          <Route path="/today-staff" element={<TodayStaff />} />
          <Route path="/today-ticket" element={<TodayTicket />} />
          <Route path="/tickets" element={<Ticket />} />
          <Route path="/customers" element={<Customer />} />
          <Route path="/performance" element={<Performance />} />
          <Route path="/report" element={<Report />} />
          <Route path="/settings" element={<Settings />} />
        </Route>
        <Route path="/*" element={<NotFound />} />
      </Routes>
    </BrowserRouter>
  </StrictMode>,
)
