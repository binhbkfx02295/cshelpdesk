export default function Performance() {
    return (
        <>
            { /* <!--    performance content--> */}
            <div className="performance-content page-main-content d-flex flex-column">
                <div id="performance-search" className="page-search d-flex flex-row align-items-center">
                    <div className="field-group mb-2 fw-bold fs-6">Chọn nhân viên để đánh giá: </div>
                    { /* <!-- ========= employee picker ========= --> */}
                    <div className="field-group username mb-2">
                        <div className="dropdown-input">
                            <input type="text" id="username" name="username" placeholder="Chọn nhân viên..." />
                                <i className="bi bi-chevron-down dropdown-button"></i>
                                <ul className="dropdown-menu">
                                    <li><a className="dropdown-item">Xóa</a></li>
                                    <li>
                                        <hr className="dropdown-divider" />
                                    </li>
                                    <li>
                                        <a className="dropdown-item">Binh 2</a>
                                    </li>
                                    <li>
                                        <a className="dropdown-item">Admin</a>
                                    </li>
                                    <li>
                                        <a className="dropdown-item">Bui Khac Binh</a>
                                    </li>
                                </ul>
                        </div>
                    </div>
                    { /* <!-- =========== month picker===========  --> */}
                    <div className="field-group month-picker mb-2">
                        <input id="month-select" type="month" className="form-control" placeholder="Chọn tháng..." />
                    </div>
                    { /* <!-- ======== search btn ===== --> */}
                    <div className="btn " id="form-submit">
                        <i className="bi bi-search me-1"></i> Tìm kiếm
                    </div>
                </div>

                <div className="performance-list data-table page-list flex-grow-1">
                    <div className="page-list-body data-body container-fluid">
                        <div className="row mb-4">
                            <div className="col">
                                <div className="card">
                                    <div className="card-header">
                                        Các tiêu chí cơ bản
                                    </div>
                                    <div className="card-body p-0">
                                        <table className="table table-hover mb-0" id="metrics-table">
                                            <thead className="table-light">
                                                <tr>
                                                    <th>Tiêu chí</th>
                                                    <th>Tham chiếu</th>
                                                    <th>Thực đạt</th>
                                                    <th>Trung bình</th>
                                                    { /* <!-- <th>Đạt</th> --> */}
                                                </tr>
                                            </thead>
                                            <tbody id="metrics-body" className="loadable">
                                                <td colSpan={999}>
                                                    <div className="text-center text-secondary py-3">Hiện chưa có đánh giá
                                                    </div>
                                                </td>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>

                        { /* <!-- 2.2 Lỗi chi tiết & ChatGPT summary --> */}
                        <div className="row mb-4">
                            <div className="col-lg-6">
                                <div className="card h-100">
                                    <div className="card-header">🔍 Chi tiết lỗi trò chuyện</div>
                                    <div className="card-body p-0">
                                        <table className="table table-hover mb-0" id="failedCriterias-table">
                                            <thead className="table-light">
                                                <tr>
                                                    <th>Tiêu chí</th>
                                                    <th>Chi tiết</th>
                                                    <th>Lượng Lỗi</th>
                                                </tr>
                                            </thead>
                                            <tbody id="failedCriterias-body" className="loadable">
                                                <td colSpan={999}>
                                                    <div className="text-center text-secondary py-3">Hiện chưa có ticket lỗi
                                                    </div>
                                                </td>
                                            </tbody>
                                        </table>
                                    </div>


                                </div>
                            </div>
                            <div className="col-lg-6">
                                <div className="card h-100 border-success">
                                    <div className="card-header bg-success text-white">💡 Đánh giá của AI</div>
                                    <div className="card-body loadable" id="chatgpt-summary"> Chưa có đánh giá
                                    </div>
                                </div>
                            </div>
                        </div>

                        { /* <!-- 2.3 Bảng ticket --> */}
                        <div className="row">
                            <div className="col">
                                <div className="card">
                                    <div className="card-header">📋 Danh sách ticket đã đánh giá</div>
                                    <div className="card-body p-0">
                                        <table className="table table-hover mb-0" id="tickets-table">
                                            <thead className="table-light">
                                                <tr>
                                                    <th>#ID</th>
                                                    <th>Nhân viên</th>
                                                    <th>Người đánh giá</th>
                                                    <th>Thời gian đánh giá</th>
                                                    <th>Đạt</th>
                                                </tr>
                                            </thead>
                                            <tbody className="loadable">
                                                <tr>
                                                    <td colSpan={999}>
                                                        <div className="text-center text-secondary py-3">Hiện chưa có ticket
                                                            lỗi</div>
                                                    </td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                { /* <!-- Modal placeholder cho chi tiết ticket --> */}
                <div className="modal fade" id="ticketModal" tabIndex={-1}>
                    <div className="modal-dialog modal-xl modal-dialog-scrollable">
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title">Chi tiết Ticket</h5>
                                <button type="button" className="btn-close" data-bs-dismiss="modal"></button>
                            </div>
                            <div className="modal-body">
                                <div className="container-fluid">
                                    <div className="row">
                                        <div className="col-7 h-100 overflow-y-auto">
                                            <div className="card mb-3">
                                                <div className="card-header">Tiêu chí lỗi</div>
                                                <div className="card-body p-0">
                                                    <table className="table table-hover mb-0">
                                                        <thead className="table-light">
                                                            <tr>
                                                                <th>Tiêu chí</th>
                                                                <th>Chi tiết</th>
                                                                <th></th>
                                                            </tr>
                                                        </thead>

                                                        <tbody id="ticket-errors-body" className="loadable">
                                                            <tr>
                                                                <td colSpan={999}>
                                                                    <div className="text-center text-secondary py-3">Hiện
                                                                        chưa
                                                                        có
                                                                        ticket lỗi
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>

                                            <div className="card mb-3 summary-container" style={{ height: '300px' }}>
                                                <div className="card-header text-white">
                                                    Nhận xét
                                                </div>
                                                <div className="card-body" style={{ overflowY: 'auto', height: '100%' }}>
                                                    <textarea className="summary"
                                                        style={{ width: '100%', height: '100%', border: 'none', resize: 'none' }}
                                                        placeholder="Nhập nội dung..."></textarea>
                                                </div>
                                            </div>

                                            <button className="btn float-end m-3" data-bs-dismiss="modal"> <i
                                                className="bi bi-x-lg me-2"></i>Hủy</button>

                                            <button className="btn float-end m-3" id="submit" disabled><i
                                                className="bi bi-check2 me-2"></i>Cập nhật</button>
                                        </div>
                                        <div className="col-5 d-flex flex-column overflow-y-auto p-3" id="chatBox">
                                            <h6 className="mb-3">Tin nhắn</h6>
                                            <div className="flex-grow-1 d-flex flex-column" id="messageList"></div>
                                        </div>


                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </>
    )
}