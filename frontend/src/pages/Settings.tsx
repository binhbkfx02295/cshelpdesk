export default function Settings() {
    return (
        <>
        {/* <!--    employee content--> */}
    <div className="employee-content page-main-content d-flex flex-column">
        <div id="employee-search" className="page-search">
            <div id="form-ticket-search"
                 className="form-ticket-search d-flex flex-row flex-wrap align-items-center">





                <div className="search-common flex-row" style={{ display: 'flex' }}>
                    <div className="search-input-group">
                        <input type="text" className="form-control form-control-sm" placeholder="Tìm tên tài khoản..."
                               id="search-keyword" />
                        <i className="bi bi-search"></i>
                    </div>

                </div>

                <div className="employee-search-btn-group ms-2">
                    <div className="btn me-1" id="employee-export-excel">
                        <i className="bi bi-download"></i> Tải xuống
                    </div>
                    <div className="btn me-1" id="employee-create">
                        <i className="bi bi-plus-circle"></i> Tạo mới
                    </div>
                </div>

            </div>
        </div>

        <div className="employee-list data-table page-list flex-grow-1">
            {/* <!-- Header --> */}
            <div className="page-list-header data-header container-fluid fw-bold border-bottom">
                <div className="row py-3">
                    <div className="col resizable sortable" data-sort="username" data-sort-direction="username"
                         style={{ position: 'relative' }}>Tài khoản
                    </div>
                    <div className="col resizable sortable" data-sort="userGroup" data-sort-direction="userGroup"
                         style={{ position: 'relative' }}><span>Chức vụ</span>

                    </div>
                    <div className="col resizable sortable" data-sort="active" data-sort-direction="active"
                         style={{ position: 'relative' }}>Trạng Thái
                    </div>
                    <div className="col resizable sortable" data-sort="contact" data-sort-direction="contact"
                         style={{ position: 'relative' }}>Email

                    </div>
                    <div className="col resizable sortable" data-sort="phone" data-sort-direction="phone"
                         style={{ position: 'relative' }}>Điện thoại
                    </div>
                    <div className="col resizable sortable" data-sort="createdAt" data-sort-direction="createdAt"
                         style={{ position: 'relative' }}>Ngày
                        tham gia
                    </div>
                    <div className="col"></div>
                </div>
            </div>


            <div className="page-list-body data-body container-fluid" id="employee-list">
            </div>
        </div>
        {/* <!-- Đặt lại mật khẩu --> */}
        <div className="modal fade" id="confirmResetPwModal" tabIndex={-1} aria-labelledby="resetPasswordModalLabel"
             aria-hidden="true">
            <div className="modal-dialog">
                <div className="modal-content">
                    <form id="resetPasswordForm">
                        <div className="modal-header">
                            <h5 className="modal-title" id="resetPasswordModalLabel">Xác nhận đặt lại mật khẩu</h5>
                            <button type="button" className="btn-close" data-bs-dismiss="modal"
                                    aria-label="Đóng"></button>
                        </div>
                        <div className="modal-body">
                            <p>Bạn có chắc muốn đặt lại mật khẩu cho người này?</p>
                            <div className="mb-3">
                                <label htmlFor="defaultPassword" className="form-label">Nhập mật khẩu mặc định </label>
                                <div className="error text-danger d-none"></div>
                                <input type="password" className="form-control" id="defaultPassword"
                                       name="defaultPassword" placeholder="Nhập mật khẩu mới..." required />
                            </div>
                        </div>
                        <div className="modal-footer">
                            <button type="button" className="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                            <button type="submit" className="btn btn-primary">Xác nhận</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        {/* <!-- Employee Detail Modal--> */}
        <div className="modal modal-md fade" id="employeeDetailModal" tabIndex={-1}
             aria-labelledby="employeeModalLabel" aria-hidden="true">
            <div className="modal-dialog">
                <div className="modal-content">
                    <div className="modal-header">
                        <h5 className="modal-title" id="employeeModalLabel">Chi Tiết Nhân viên</h5>
                        <button type="button" className="btn-close" data-bs-dismiss="modal"
                                aria-label="Đóng"></button>
                    </div>
                    <div className="modal-body">
                        <div className="d-flex flex-column">
                            <div className="detail-info flex-grow-1">
                                <div className="mb-2 field-group username">
                                    <label htmlFor="username">Tên tài khoản</label>
                                    <input type="text" id="username" disabled />
                                </div>
                                <div className="mb-2 field-group userGroup">
                                            <span className="fw-medium field-name">
                                                Vai trò:
                                            </span>
                                    <div className="dropdown-input">
                                        <input type="text" id="edit_userGroup" placeholder="Chọn ..." />
                                        <i className="bi bi-chevron-down dropdown-button"></i>
                                        <ul className="dropdown-menu">
                                        </ul>
                                    </div>
                                </div>
                                <div className="mb-2 field-group active">
                                            <span className="fw-medium field-name">
                                                Trạng thái:
                                            </span>
                                    <div className="dropdown-input field-group">
                                        <input type="text" id="edit_active" placeholder="Chọn ..." />
                                        <i className="bi bi-chevron-down dropdown-button"></i>
                                        <ul className="dropdown-menu">
                                            <a href="#" className="dropdown-item" data-active="true">Hoạt động</a>
                                            <a href="#" className="dropdown-item" data-active="false">Hủy kích
                                                hoạt</a>
                                        </ul>
                                    </div>
                                </div>
                                <div className="field-group createdAt mb-2">
                                    <label htmlFor="createdAt">Ngày tham gia</label>
                                    <input type="text" id="createdAt" placeholder="" disabled />
                                </div>

                                <div className="field-group name mb-2">
                                    <label htmlFor="name">Họ tên</label>
                                    <input type="text" id="name" placeholder="Nhập họ tên..." />
                                </div>
                                <div className="field-group email mb-2">
                                    <label htmlFor="email">Email</label>
                                    <input type="text" id="email" placeholder="Nhập email..." />
                                </div>
                                <div className="field-group phone mb-2">
                                    <label htmlFor="phone">Phone</label>
                                    <input type="text" id="phone" placeholder="Nhập Số điện thoại..." />
                                </div>
                                <div className="field-group description mb-2">
                                    <label htmlFor="description">Mô tả</label>
                                    <input type="text" id="description" placeholder="Nhập mô tả..." />
                                </div>

                            </div>
                            <div className="modal-footer">
                                <button className="btn" id="cancel-edit"><i
                                        className="bi bi-arrow-clockwise me-2"></i>Hủy</button>
                                <button className="btn" id="submit-edited" disabled><i
                                        className="bi bi-save me-2"></i>Cập
                                    nhật</button>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>
        {/* <!-- Add Employee Modal--> */}
        <div className="modal modal-md fade" id="addEmployeeModal" tabIndex={-1}
             aria-labelledby="addEmployeeModalLabel" aria-hidden="true">
            <div className="modal-dialog">
                <div className="modal-content">
                    <div className="modal-header">
                        <h5 className="modal-title" id="ticketFullDetailModalLabel">Tạo mới Nhân viên</h5>
                        <button type="button" className="btn-close" data-bs-dismiss="modal"
                                aria-label="Đóng"></button>
                    </div>

                    <div className="modal-body">
                        <div className="field-group">
                            <label htmlFor="create-username">Tên tài khoản</label>
                            <input type="text" id="create-username" placeholder="Tên tài khoản" />
                        </div>

                        <div className="field-group">
                            <label htmlFor="create-password">Mật khẩu mặc định</label>
                            <input type="password" id="create-password" placeholder="Mật khẩu.." />
                        </div>

                        <div className="field-group userGroup">
                            <label htmlFor="create_userGroup">Vai trò</label>
                            <div className="dropdown-input">
                                <input type="text" id="create_userGroup" placeholder="Chọn ..." />
                                <i className="bi bi-chevron-down dropdown-button"></i>
                                <ul className="dropdown-menu">
                                </ul>
                            </div>
                        </div>

                        <div className="field-group">
                            <label htmlFor="create-name">Họ tên</label>
                            <input type="text" id="create-name" placeholder="Họ tên.." />
                        </div>

                        <div className="field-group">
                            <label htmlFor="create-phone">Số điện thoại</label>
                            <input type="text" id="create-phone" placeholder="Số điện thoại.." />
                        </div>

                        <div className="field-group">
                            <label htmlFor="create-email">Email</label>
                            <input type="text" id="create-email" placeholder="Email..." />
                        </div>

                        <div className="field-group">
                            <label htmlFor="create-description">Mô tả</label>
                            <input type="textarea" id="create-description" placeholder="Mô tả..." />
                        </div>

                    </div>

                    <div className="modal-footer">
                        <button type="button" className="btn btn-secondary" data-bs-dismiss="modal"><i
                                className="bi bi-x me-1"></i>Hủy</button>
                        <button type="button" className="btn btn-primary" id="submit-create-employee"><i
                                className="bi bi-check-lg"></i>Xác Nhận</button>
                    </div>

                </div>
            </div>
        </div>
        <footer className="footer mt-auto py-3 text-center">
            <span>© 2025 Thiên An Phú - Customer Help Desk</span>
        </footer>
    </div>
        </>
    )
}