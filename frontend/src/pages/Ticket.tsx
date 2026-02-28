export default function Ticket() {
    return (
        <>
            {/*<!-- Ticket Manager Content --> */}
            <div className="ticket-manager-content page-main-content d-flex flex-column">
                <div id="ticket-search" className="page-search">
                    <div id="form-ticket-search" className="form-ticket-search d-flex flex-row flex-wrap">
                        <div className="field-group title mb-2">
                            <label htmlFor="title">Tiêu Đề</label>
                            <input type="text" id="title" name="title" placeholder="Nhập tiêu đề..." />
                        </div>

                        <div className="field-group assignee mb-2">
                            <label htmlFor="assignee">Assignee</label>
                            <div className="dropdown-input">
                                <input type="text" id="assignee" name="assignee" />
                                    <i className="bi bi-chevron-down dropdown-button"></i>
                                    <ul className="dropdown-menu">
                                        <li><a className="dropdown-item">Action</a></li>
                                        <li><a className="dropdown-item">Another action</a></li>
                                        <li><a className="dropdown-item">Something else here</a></li>
                                        <li><a className="dropdown-item">Separated link</a></li>
                                    </ul>
                            </div>
                        </div>
                        <div className="field-group facebookuser mb-2">
                            <label htmlFor="facebookuser">Facebook User</label>
                            <input type="text" id="facebookuser" name="facebookuser" placeholder="Nhập ID..." />
                        </div>



                        <div className="field-group category mb-2">
                            <label htmlFor="category">Phân loại</label>
                            <div className="dropdown-input">
                                <input type="text" id="category" name="category" placeholder="Chọn danh mục..." />
                                    <i className="bi bi-chevron-down dropdown-button"></i>
                                    <ul className="dropdown-menu">
                                    </ul>
                            </div>
                        </div>


                        <div className="field-group progress-status mb-2">
                            <label htmlFor="progress-status">Tình trạng xử lý</label>
                            <div className="dropdown-input">
                                <input type="text" id="progress-status" name="progress-status" placeholder="Chọn ..." />
                                    <i className="bi bi-chevron-down dropdown-button"></i>
                                    <ul className="dropdown-menu">
                                    </ul>
                            </div>
                        </div>


                        <div className="field-group emotion mb-2">
                            <label htmlFor="emotion">Cảm xúc</label>
                            <div className="dropdown-input">
                                <input type="text" id="emotion" name="emotion" placeholder="Chọn loại cảm xúc..." />
                                    <i className="bi bi-chevron-down dropdown-button"></i>
                                    <ul className="dropdown-menu">
                                    </ul>
                            </div>
                        </div>

                        <div className="field-group satisfaction mb-2">
                            <label htmlFor="satisfaction">Mức hài lòng</label>
                            <div className="dropdown-input">
                                <input type="text" id="satisfaction" name="satisfaction" placeholder="Chọn mức hài lòng ..." />
                                    <i className="bi bi-chevron-down dropdown-button"></i>
                                    <ul className="dropdown-menu">
                                    </ul>
                            </div>
                        </div>

                        <div className="field-group date-range mb-2">
                            {/* <!-- From - To --> */}
                            <div className="d-flex gap-2">
                                <div>
                                    <label htmlFor="fromDate">Từ </label>
                                    <input type="date" id="fromDate" name="fromDate" className="form-control" required />
                                </div>
                                <div>
                                    <label htmlFor="toDate">Đến </label>
                                    <input type="date" id="toDate" name="toDate" className="form-control" required />
                                </div>
                                <div className="dropdown-input mb-1 fast-pick">
                                    <label htmlFor="dateRangeLabel">Chọn nhanh</label>
                                    <div id="dateRangeLabel-container" className="position-relative">
                                        <input type="text" id="dateRangeLabel" readOnly value="Thời gian" />
                                            <i className="bi bi-chevron-down dropdown-button"></i>
                                    </div>
                                    <ul className="dropdown-menu">
                                        <li><a className="dropdown-item" data-range="today">Hôm nay</a></li>
                                        <li><a className="dropdown-item" data-range="yesterday">Hôm qua</a></li>
                                        <li><a className="dropdown-item" data-range="this_week">Tuần này</a></li>
                                        <li><a className="dropdown-item" data-range="last_7_days">7 Ngày</a></li>
                                        <li><a className="dropdown-item" data-range="this_month">Tháng này</a></li>
                                        <li><a className="dropdown-item" data-range="last_30_days">30 Ngày</a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>

                        <div className="form-submit-group d-flex gap-2 align-items-center">
                            <div className="btn " id="form-submit">
                                <i className="bi bi-search me-1"></i> Tìm kiếm
                            </div>

                            <div className="btn" id="form-reset">
                                <i className="bi bi-arrow-repeat me-1"></i> Làm mới
                            </div>

                            <div className="btn" id="form-export-excel">
                                <i className="bi bi-download"></i> Tải xuống
                            </div>

                            <div className="btn" id="form-create-ticket">
                                <i className="bi bi-plus-circle"></i> Tạo mới
                            </div>
                        </div>

                    </div>
                </div>

                <div id="ticket-list" className="page-list data-table flex-grow-1">
                    {/* <!-- Header --> */}
                    <div className="page-list-header data-header container-fluid fw-bold border-bottom">
                        <div className="row">
                            <div className="col resizable sortable" data-sort="id" data-sort-direction="none">#ID</div>
                            <div className="col resizable sortable" data-sort="title" data-sort-direction="none">Tiêu đề</div>
                            <div className="col resizable sortable" data-sort="assignee" data-sort-direction="none">Nhân viên</div>
                            <div className="col resizable sortable" data-sort="facebookUser" data-sort-direction="none">Facebook User
                            </div>
                            <div className="col resizable sortable" data-sort="progress-status" data-sort-direction="none">Trạng thái
                            </div>
                            <div className="col resizable sortable" datCa-sort="category" data-sort-direction="none">Danh mục</div>
                            <div className="col resizable sortable" data-sort="createdA" data-sort-direction="none">Ngày tạo</div>
                            <div className="col resizable sortable" data-sort="emotion" data-sort-direction="none">Cảm xúc</div>
                            <div className="col resizable sortable" data-sort="satisfaction" data-sort-direction="none">Mức hài lòng
                            </div>
                        </div>
                    </div>

                    <div className="page-list-body data-body container-fluid" id="ticket-list-body"></div>

                    <div id="ticket-pagination-bar" className="page-list-footer page-pagination bg-white p-2 border-top d-flex justify-content-between align-items-center">
                        <div className="form-group d-flex align-items-center">
                            <label htmlFor="pageSize" className="me-2 mb-0">Hiển thị:</label>
                            <select id="pageSize" className="form-select form-select-sm" style={{ width: "auto" }}>
                                <option value="10">10</option>
                                <option value="20">20</option>
                                <option value="50">50</option>
                                <option value="100">100</option>
                            </select>
                            <span className="ms-2">mỗi trang</span>
                        </div>
                        <nav>
                            <ul id="pagination-menu" className="pagination pagination-sm mb-0">
                            </ul>
                        </nav>
                    </div>

                </div>


                {/* <!-- Ticket Creation --> */}
                <div id="createTicketModal" className="modal fade" tabIndex={-1}>
                    <div className="modal-dialog modal-lg modal-dialog-centered">
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title">Tạo Ticket Mới</h5>
                                <button type="button" className="btn-close" data-bs-dismiss="modal"></button>
                            </div>
                            <div className="modal-body">
                                <form id="createTicketForm">
                                    <div className="field-group title mb-2">
                                        <label htmlFor="create_title">Tiêu Đề</label>
                                        <input type="text" id="create_title" name="title" placeholder="Nhập tiêu đề..." />
                                    </div>

                                    <div className="field-group assignee mb-2">
                                        <label htmlFor="create_assignee">Assignee</label>
                                        <div className="dropdown-input">
                                            <input type="text" id="create_assignee" name="assignee" />
                                                <i className="bi bi-chevron-down dropdown-button"></i>
                                                <ul className="dropdown-menu">
                                                    <li><a className="dropdown-item">Action</a></li>
                                                    <li><a className="dropdown-item">Another action</a></li>
                                                    <li><a className="dropdown-item">Something else here</a></li>
                                                    <li><a className="dropdown-item">Separated link</a></li>
                                                </ul>
                                        </div>
                                    </div>
                                    <div className="field-group facebookuser mb-2">
                                        <label htmlFor="create_facebookuser">Facebook User</label>
                                        <input type="text" id="create_facebookuser" name="facebookuser" placeholder="Nhập ID..." />
                                    </div>



                                    <div className="field-group category mb-2">
                                        <label htmlFor="create_category">Phân loại</label>
                                        <div className="dropdown-input">
                                            <input type="text" id="create_category" name="category" placeholder="Chọn danh mục..." />
                                                <i className="bi bi-chevron-down dropdown-button"></i>
                                                <ul className="dropdown-menu">
                                                </ul>
                                        </div>
                                    </div>


                                    <div className="field-group progress-status mb-2">
                                        <label htmlFor="create_progress-status">Tình trạng xử lý</label>
                                        <div className="dropdown-input">
                                            <input type="text" id="create_progress-status" name="progress-status" placeholder="Chọn ..." />
                                                <i className="bi bi-chevron-down dropdown-button"></i>
                                                <ul className="dropdown-menu">
                                                </ul>
                                        </div>
                                    </div>


                                    <div className="field-group emotion mb-2">
                                        <label htmlFor="create_emotion">Cảm xúc</label>
                                        <div className="dropdown-input">
                                            <input type="text" id="create_emotion" name="emotion" placeholder="Chọn loại cảm xúc..." />
                                                <i className="bi bi-chevron-down dropdown-button"></i>
                                                <ul className="dropdown-menu">
                                                </ul>
                                        </div>
                                    </div>

                                    <div className="field-group satisfaction mb-2">
                                        <label htmlFor="create_satisfaction">Mức hài lòng</label>
                                        <div className="dropdown-input">
                                            <input type="text" id="create_satisfaction" name="satisfaction" placeholder="Chọn mức hài lòng ..." />
                                                <i className="bi bi-chevron-down dropdown-button"></i>
                                                <ul className="dropdown-menu">
                                                </ul>
                                        </div>
                                    </div>
                                </form>
                            </div>
                            <div className="modal-footer">
                                <button type="button" className="btn btn-outline-secondary" id="resetCreateTicket">
                                    <i className="bi bi-arrow-counterclockwise me-1"></i> Làm mới
                                </button>
                                <button type="button" className="btn btn-secondary" data-bs-dismiss="modal"><i className="bi bi-x me-1"></i>Hủy</button>
                                <button type="button" className="btn btn-primary" id="submitCreateTicket"><i className="bi bi-plus-circle me-1"></i>Tạo Ticket</button>
                            </div>
                        </div>
                    </div>
                </div>

                {/* <!-- Ticket Detail Modal--> */}
                <div id="ticketFullDetailModal" className="modal fade ticket-detail-modal" tabIndex={-1} aria-labelledby="ticketFullDetailModalLabel" aria-hidden="true">
                    <div className="modal-dialog modal-xl modal-dialog-scrollable">
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title" id="ticketFullDetailModalLabel">Chi Tiết Toàn Bộ Ticket</h5>
                                <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                            </div>
                            <div className="modal-body p-0">
                                <div className="container-fluid p-0">
                                    <div className="row g-0" style={{ height: '80vh' }}>
                                        <div className="col-3 border-end d-flex flex-column overflow-auto p-3" id="ticketInfoColumn">
                                            <h6 className="mb-3">Thông tin Ticket</h6>
                                            <div className="field-group ticketId mb-2">
                                                <label htmlFor="editTicketId">#ID</label>
                                                <input type="text" id="editTicketId" name="ticketId" disabled />
                                            </div>

                                            <div className="field-group title mb-2">
                                                <label htmlFor="editTitle">Tiêu Đề</label>
                                                <input type="text" id="editTitle" name="editTitle" placeholder="Nhập tiêu đề..." />
                                            </div>
                                            <div className="field-group facebookuser mb-2">
                                                <label htmlFor="editFacebookUser">Facebook User</label>
                                                <input type="text" id="editFacebookUser" name="facebookuser" disabled />
                                            </div>

                                            <div className="field-group assignee mb-2">
                                                <label htmlFor="editAssignee">Nhân viên</label>
                                                <input type="text" id="editAssignee" name="assignee" disabled />
                                            </div>

                                            <div className="field-group createdAt mb-2">
                                                <label htmlFor="editCreatedAt">Ngày tạo</label>
                                                <div className="dropdown-input">
                                                    <input type="text" id="editCreatedAt" name="assignee" disabled />
                                                </div>
                                            </div>

                                            <div className="field-group category mb-2">
                                                <label htmlFor="editCategory">Phân loại</label>
                                                <div className="dropdown-input">
                                                    <input type="text" id="editCategory" name="category" placeholder="Chọn danh mục..." />
                                                        <i className="bi bi-chevron-down dropdown-button"></i>
                                                        <ul className="dropdown-menu">
                                                        </ul>
                                                </div>
                                            </div>

                                            <div className="field-group progress-status mb-2">
                                                <label htmlFor="editProgressStatus">Tình trạng xử lý</label>
                                                <div className="dropdown-input">
                                                    <input type="text" id="editProgressStatus" name="progress-status" placeholder="Chọn trạng thái..." />
                                                        <i className="bi bi-chevron-down dropdown-button"></i>
                                                        <ul className="dropdown-menu">
                                                        </ul>
                                                </div>
                                            </div>


                                            <div className="field-group emotion mb-2">
                                                <label htmlFor="editEmotion">Cảm xúc</label>
                                                <input type="text" id="editEmotion" name="emotion" disabled />
                                            </div>

                                            <div className="field-group satisfaction mb-2">
                                                <label htmlFor="satisfaction">Mức hài lòng</label>
                                                <input type="text" id="editSatisfaction" name="satisfaction" disabled />
                                            </div>

                                            <div className="field-group tag mb-2">
                                                <label className="form-label">Tag</label>
                                                <select className="form-select" id="editTags" multiple></select>
                                            </div>

                                            <div className="field-group note mb-2">
                                                <label className="form-label" htmlFor="editNote">Ghi chú</label>
                                                <textarea className="form-control" id="editNote" rows={4}></textarea>
                                            </div>

                                            <div className="mt-3 d-flex justify-content-end gap-2" id="ticketEditFooter">
                                                <button type="button" className="btn btn-secondary btn-sm" id="cancelEdit">Hủy</button>
                                                <button type="button" className="btn btn-primary btn-sm" id="saveEdit">Cập
                                                    nhật</button>
                                            </div>
                                        </div>
                                        <div className="col-6 d-flex flex-column overflow-auto p-3" id="chatBox">
                                            <h6 className="mb-3">Tin nhắn</h6>
                                            <div className="flex-grow-1 d-flex flex-column" id="messageList"></div>
                                        </div>
                                        <div className="col-3 border-start d-flex flex-column overflow-auto p-3" id="ticketHistory">
                                            <h6 className="mb-3">Lịch sử Ticket</h6>
                                            <ul className="list-group" id="historyList"></ul>
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