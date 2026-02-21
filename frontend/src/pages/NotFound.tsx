import Footer from "../components/layout/Footer";
import Header from "../components/layout/Header";
import Sidebar from "../components/layout/Sidebar";

export default function NotFound() {
    return (
        <>
            <div className="app-container">
                <div className="page-content">
                    <Sidebar />
                    <Header />


                    {/* <!-- Error Content --> */}
                    <div className="error-content text-center" style={{ marginTop: "80px" }}>
                        <h1 style={{ fontSize: "64px", color: "#dc3545" }}>404</h1>
                        <p style={{ fontSize: "24px" }}>Trang bạn tìm kiếm không tồn tại.</p>
                        <a href="/" className="btn btn-primary mt-3">Quay lại Trang chính</a>
                    </div>
                    <Footer />
                </div>
            </div>21
        </>
    )
}