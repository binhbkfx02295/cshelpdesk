import { Outlet } from "react-router";
import Sidebar from "./Sidebar";
import Header from "./Header";
import Footer from "./Footer";

export default function Layout() {
    return (
        <>
            <div className="app-container">
                <div className="page-content">
                    <Sidebar />
                    <Header />

                    <Outlet />
                    <Footer />
                </div>
            </div>
        </>
    );
}