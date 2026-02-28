export default function Customer() {
    return (
        <>
            <div className="customer-content page-main-content d-flex flex-column">
        <div id="customer-search" className="page-search">
            <div id="form-ticket-search" className="form-ticket-search d-flex flex-row flex-wrap align-items-center">

                <div className="select-all me-2 d-flex align-items-center">
                    <input type="checkbox" className="me-3" />
                    <span className="me-2 pe-3">Chọn tất cả</span>
                </div>

                <div className="btn me-3" id="delete-checkbox">Xóa</div>

                <div className="search-common flex-row" style={{ display: 'flex' }}>
                    <div className="search-input-group">
                        <input type="text" className="form-control form-control-sm" placeholder="Tìm kiếm..." id="search-keyword" />
                        <i className="bi bi-search"></i>
                    </div>
                    <select className="form-select form-select-sm" aria-label=".form-select-lg" id="search-field">
                        <option value="facebookId" selected>Facebook ID</option>
                        <option value="facebookName">Tên Facebook</option>
                        <option value="realName">Họ tên</option>
                        <option value="phone">Số điện thoại</option>
                        <option value="email">Email</option>
                        <option value="zalo">Zalo</option>
                    </select>
                </div>

                <div className="customer-search-btn-group ms-2">
                    <div className="btn me-1" id="customer-export-excel">
                        <i className="bi bi-download"></i> Tải xuống
                    </div>
                </div>

            </div>
        </div>

        <div className="customer-list data-table page-list flex-grow-1">
            {/* Header */} 
            <div className="page-list-header data-header container-fluid fw-bold border-bottom">
                <div className="row">
                    <div className="col"></div>
                    <div className="col resizable sortable" data-sort="facebookId" data-sort-direction="none">Facebook ID
                    </div>
                    <div className="col resizable sortable" data-sort="title" data-sort-direction="none">Tên Facebook</div>
                    <div className="col resizable sortable" data-sort="assignee" data-sort-direction="none">Họ Tên</div>
                    <div className="col resizable sortable" data-sort="facebookUser" data-sort-direction="none">Số Điện Thoại
                    </div>
                    <div className="col resizable sortable" data-sort="progress-status" data-sort-direction="none">Email
                    </div>
                    <div className="col resizable sortable" data-sort="category" data-sort-direction="none">Zalo</div>
                    <div className="col"></div>
                </div>
            </div>

            <div className="page-list-body data-body container-fluid" id="customer-list-body">
                <div id="loading-result" className="row loading-row justify-content-center text-muted py-3" style={{ display: 'none' }}>
                    <div className="col-auto">
                        <div className="spinner-border spinner-border-sm me-2 text-primary" role="status"></div>
                        Đang tải dữ liệu...
                    </div>
                </div>
                <div id="no-customer-result" className="text-center text-muted py-3" style={{ display: 'none' }}>
                    <i className="bi bi-inbox me-1"></i> Hiện chưa có dữ liệu khách hàng.
                </div>

            </div>

            <div id="customer-pagination-bar" className="page-list-footer page-pagination bg-white p-2 border-top d-flex justify-content-between align-items-center">
                <div className="form-group d-flex align-items-center">
                    <label htmlFor="pageSize" className="me-2 mb-0">Hiển thị:</label>
                    <select id="pageSize" className="form-select form-select-sm" style={{ width: 'auto' }}>
                        <option value="10">10</option>
                        <option value="20">20</option>
                        <option value="50">50</option>
                        <option value="100">100</option>
                    </select>
                    <span className="ms-2">mỗi trang</span>
                </div>
                <nav>
                    <ul id="pagination-menu" className="pagination pagination-sm mb-0">
                        {/* Các nút phân trang sẽ được thêm bằng JS */}
                    </ul>
                </nav>
            </div>

        </div>

        {/* Customer Detail Modal*/}
        <div className="modal modal-xl fade" id="customerDetailModal" tabIndex={-1} aria-labelledby="customerEditModalLabel" aria-hidden="true" data-facebookid="">
            <div className="modal-dialog">
                <div className="modal-content">
                    <div className="modal-header">
                        <h5 className="modal-title" id="ticketFullDetailModalLabel">Chi Tiết Khách hàng</h5>
                        <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                    </div>
                    <div className="modal-body">
                        <div className="d-flex flex-row h-100">
                            {/* customer profile */}
                            <div className="customer-profile w-100% d-flex flex-column">
                                <div className="facebook-profile d-flex flex-column align-items-center justify-content-end mb-3">
                                    <div className="img-contaier">
                                        <img id="facebookProfilePic" src="img/facebookuser-profile-placeholder.jpg" alt="" />
                                    </div>
                                    <div className="bg-cover"></div>
                                    <div className="w-100 text-center">
                                        <span id="facebookName">- -</span>

                                    </div>
                                    <span id="facebookId">- -</span>
                                </div>
                                <div className="d-flex flex-row fs-5 loading-row justify-content-center text-muted py-3">
                                    <div className="text-center">
                                        <div className="spinner-border spinner-border-sm me-2 text-primary" role="status"></div>
                                        Đang tải dữ liệu...
                                    </div>
                                </div>
                                <div className="d-flex flex-column d-none">
                                    <div className="detail-info flex-grow-1">
                                        <div className="mb-2">
                                                    <span className="fw-medium field-name">
                                                        <i className="bi bi-person-fill me-2"></i>Họ tên:
                                                    </span>
                                            <span className="field-value float-end">- -</span>
                                            <input type="text" id="realName" className="float-end d-none" data-original="Bùi Khắc Bình" />
                                        </div>
                                        <div className="mb-2">
                                                    <span className="fw-medium field-name">
                                                        <i className="bi bi-telephone-fill me-2"></i>Số điện thoại:
                                                    </span>
                                            <span className="field-value float-end">- -</span>
                                            <input type="text" id="phone" className="float-end d-none" data-original="0797409134" />
                                        </div>
                                        <div className="mb-2">
                                                    <span className="fw-medium field-name">
                                                        <i className="bi bi-envelope-fill me-2"></i>Email:
                                                    </span>
                                            <span className="field-value float-end">- -</span>
                                            <input type="text" id="email" className="float-end d-none" data-original="teddie.bbui@gmail.com" />
                                        </div>
                                        <div className="mb-2">
                                                    <span className="fw-medium field-name">
                                                        <i className="bi bi-chat-dots-fill me-2"></i>Zalo:
                                                    </span>
                                            <span className="field-value float-end">- -</span>
                                            <input type="text" id="zalo" className="float-end d-none" data-original="0797409134" />
                                        </div>
                                        <div className="mb-2">
                                                    <span className="fw-medium field-name">
                                                        <i className="bi bi-calendar2-week-fill me-2"></i>Ngày sinh:
                                                    </span>
                                            <span className="float-end">- -</span>
                                        </div>
                                        <div className="mb-2">
                                                    <span className="fw-medium field-name">
                                                        <i className="bi bi-geo-alt-fill me-2"></i>Địa chỉ:
                                                    </span>
                                            <span className="float-end">- -</span>
                                        </div>
                                    </div>
                                    <div className="text-end">
                                        <div id="edit">
                                            <i className="bi bi-pencil-square"></i>
                                        </div>
                                        <div id="edited" className="d-none">
                                            <button className="btn" id="cancel-edit"><i className="bi bi-arrow-clockwise me-2"></i>Hủy</button>
                                            <button className="btn" id="submit-edited" disabled ><i className="bi bi-save me-2"></i>Cập nhật</button>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            {/* <!-- customer history ticket --> */}
                            <div className="customer-history-ticket flex-grow-1 d-flex flex-column">

                                <h3 id="title" className="py-3">Ticket Gần Đây</h3>
                                <div className="data-table flex-grow-1 overflow-y-scroll" style={{ minHeight: 0 }}>
                                    <div className="item">
                                        <div className="d-flex flex-row">
                                            <div className="i-badge me-3">
                                                <i className="bi bi-ticket-perforated"></i>
                                            </div>
                                            <div className="">
                                                <div>
                                                    <div className="title mb-2">Chưa có tiêu đề <span className="ticketId"> -
                                                                    #ID: <span id="ticketId">98956859685465</span></span></div>
                                                    <div className="assignee mb-1">Admin - <span className="category">Chưa phân loại</span></div>
                                                    <div className="createdAt mb-1">21:57:09 - 17/05/2025</div>
                                                </div>
                                            </div>
                                            <div className="ms-auto text-start d-flex flex-column justify-content-evenly">
                                                <div className=""><i className="bi bi-activity me-3"></i><span className="progressStatus pending">Đang xử lý</span></div>
                                                <div className="emotion"><i className="bi bi-emoji-smile me-3"></i>Rất
                                                    tức giận</div>
                                                <div className="satisfaction"><i className="bi bi-stars me-3"></i>Hài
                                                    lòng</div>
                                            </div>
                                        </div>
                                    </div>

                                    <div className="item">
                                        <div className="d-flex flex-row">
                                            <div className="i-badge me-3">
                                                <i className="bi bi-ticket-perforated"></i>
                                            </div>
                                            <div className="">
                                                <div>
                                                    <div className="title mb-2">Chưa có tiêu đề <span className="ticketId"> -
                                                                    #ID: <span id="ticketId">98956859685465</span></span></div>
                                                    <div className="assignee mb-1">Admin - <span className="category">Chưa phân loại</span></div>
                                                    <div className="createdAt mb-1">21:57:09 - 17/05/2025</div>
                                                </div>
                                            </div>
                                            <div className="ms-auto text-start d-flex flex-column justify-content-evenly">
                                                <div className=""><i className="bi bi-activity me-3"></i><span className="progressStatus resolved">Đã xử lý</span></div>
                                                <div className="emotion"><i className="bi bi-emoji-smile me-3"></i>Rất
                                                    tức giận</div>
                                                <div className="satisfaction"><i className="bi bi-stars me-3"></i>Hài
                                                    lòng</div>
                                            </div>
                                        </div>
                                    </div>
                                    <div className="item">
                                        <div className="d-flex flex-row">
                                            <div className="i-badge me-3">
                                                <i className="bi bi-ticket-perforated"></i>
                                            </div>
                                            <div className="">
                                                <div>
                                                    <div className="title mb-2">Chưa có tiêu đề <span className="ticketId"> -
                                                                    #ID: <span id="ticketId">98956859685465</span></span></div>
                                                    <div className="assignee mb-1">Admin - <span className="category">Chưa phân loại</span></div>
                                                    <div className="createdAt mb-1">21:57:09 - 17/05/2025</div>
                                                </div>
                                            </div>
                                            <div className="ms-auto text-start d-flex flex-column justify-content-evenly">
                                                <div className=""><i className="bi bi-activity me-3"></i><span className="progressStatus on-hold">Đang chờ</span></div>
                                                <div className="emotion"><i className="bi bi-emoji-smile me-3"></i>Rất
                                                    tức giận</div>
                                                <div className="satisfaction"><i className="bi bi-stars me-3"></i>Hài
                                                    lòng</div>
                                            </div>
                                        </div>
                                    </div>
                                    <div className="item">
                                        <div className="d-flex flex-row">
                                            <div className="i-badge me-3">
                                                <i className="bi bi-ticket-perforated"></i>
                                            </div>
                                            <div className="ticket">
                                                <div className="title">Chưa có tiêu đề</div>
                                                <div className="assignee">Admin</div>
                                                <div className="assignee">21:57:09 - 17/05/2025</div>
                                            </div>
                                        </div>
                                    </div>
                                    <div className="item">
                                        <div className="d-flex flex-row">
                                            <div className="i-badge me-3">
                                                <i className="bi bi-ticket-perforated"></i>
                                            </div>
                                            <div className="ticket">
                                                <div className="title">Chưa có tiêu đề</div>
                                                <div className="assignee">Admin</div>
                                                <div className="assignee">21:57:09 - 17/05/2025</div>
                                            </div>
                                        </div>
                                    </div>
                                    <div className="item">
                                        <div className="d-flex flex-row">
                                            <div className="i-badge me-3">
                                                <i className="bi bi-ticket-perforated"></i>
                                            </div>
                                            <div className="ticket">
                                                <div className="title">Trả hàng</div>
                                                <div className="assignee">Bui Khac Binh</div>
                                                <div className="assignee">11:25:09 - 16/05/2025</div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </div>

                </div>
            </div>
        </div>

        {/* <!-- Customer delete modal --> */}
        <div className="modal fade" id="customerDeleteModal" tabIndex={-1} aria-labelledby="customerDeleteModalLabel" aria-hidden="true">
            <div className="modal-dialog">
                <div className="modal-content">
                    <div className="modal-header">
                        <h5 className="modal-title" id="customerDeleteModalLabel">Xác nhận xoá khách hàng</h5>
                        <button type="button" className="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Đóng"></button>
                    </div>
                    <div className="modal-body">
                        <p>Bạn có chắc chắn muốn xoá khách hàng <strong id="delete-customer-name"></strong>
                            không?</p>
                        <p>Hành động này không thể hoàn tác.</p>
                    </div>
                    <div className="modal-footer">
                        <button type="button" className="btn" data-bs-dismiss="modal">Huỷ</button>
                        <button type="button" className="btn" id="confirmDeleteCustomerBtn">Xoá</button>
                    </div>
                </div>
            </div>
        </div>

    </div>
        </>
    )
}