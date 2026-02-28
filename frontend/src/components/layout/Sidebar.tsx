export default function Sidebar() {
    return (
        <>
            {/* <!-- Sidebar --> */}
            <div className="sidebar" id="sidebar">

                <div className="sidebar-header">
                    <div className="logo">
                        <i className="bi bi-shield-plus"></i>
                        <span>Help Desk</span>
                    </div>
                    <button className="btn-toggle-sidebar d-lg-none" id="toggleSidebar">
                        <i className="bi bi-x-lg"></i>
                    </button>
                </div>

                <ul className="sidebar-menu">
                    <div className="sidebar-heading">
                        <div className="title">BẢNG ĐIỀU KHIỂN </div>
                        <div className="label">Theo dõi thời gian thực</div>
                    </div>
                    <li>
                        <a href="/today-staff">
                            <i className="bi bi-speedometer2"></i>
                            <span>Bảng Điều Khiển</span>
                            <i className="bi bi-arrow-right-short"></i>
                        </a>
                    </li>
                    <li className="">
                        <a href="/today-ticket">
                            <i className="bi bi-ticket-perforated"></i>
                            <span>Ticket hôm nay</span>
                            <i className="bi bi-arrow-right-short"></i>
                        </a>
                    </li>

                    <div className="sidebar-heading">
                        <div className="title">DỮ LIỆU </div>
                        <div className="label">Quản lý & tra cứu dữ liệu</div>
                    </div>

                    <li>
                        <a href="/tickets">
                            <i className="bi bi-ticket-perforated"></i>
                            <span>Quản Lý Ticket</span>
                            <i className="bi bi-arrow-right-short"></i>
                        </a>
                    </li>
                    <li >
                        <a href="/customers">
                            <i className="bi bi-people"></i>
                            <span>Người Dùng</span>
                            <i className="bi bi-arrow-right-short"></i>
                        </a>
                    </li>
                    <div className="sidebar-heading">
                        <div className="title">BÁO CÁO </div>
                        <div className="label">Phân tích xu hướng & dữ liệu</div>
                    </div>
                    <li  >
                        <a href="/performance">
                            <i className="bi bi-graph-up"></i>
                            <span>Hiệu Suất</span>
                            <i className="bi bi-arrow-right-short"></i>
                        </a>
                    </li>
                    <li  >
                        <a href="/report">
                            <i className="bi bi-bar-chart"></i>
                            <span>Báo Cáo</span>
                            <i className="bi bi-arrow-right-short"></i>
                        </a>
                    </li>

                    <div className="sidebar-heading">
                        <div className="title">CÀI ĐẶT</div>
                        <div className="label">Cấu hình hệ thống</div>
                    </div>
                    <li  >
                        <a href="/settings">
                            <i className="bi bi-person-badge"></i>
                            <span>Nhân viên</span>
                            <i className="bi bi-arrow-right-short"></i>
                        </a>
                    </li>
                </ul>
            </div>

        </>
    );
}