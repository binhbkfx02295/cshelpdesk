export default function TodayStaff() {
    return (
        <>
            <div className="dashboard-content page-main-content d-flex flex-column">
                {/* <!-- Employee List Section --> */}
                <div className="mb-4" id="employeeSection">
                    <div className="card employee-card">
                        <div className="card-header">
                            <h3 className="card-title">
                                <i className="bi bi-people-fill"></i>
                                Danh Sách Nhân Viên
                            </h3>
                            <span className="badge bg-primary employee-count">2</span>
                        </div>
                        <table id="employeeTable" className="table table-hover">
                            <thead>
                                <tr>
                                    <th data-sort="name">Tên nhân viên</th>
                                    <th data-sort="userGroup">Vai trò</th>
                                    <th data-sort="ticketCount">Số lượng ticket</th>
                                    <th data-sort="status">Trạng thái</th>
                                    <th data-sort="statusTime">Thời gian trạng thái</th>
                                </tr>
                            </thead>



                            <tbody id="employeeList2" className="position-relative">
                                
                            </tbody>

                        </table>

                    </div>
                </div>
            </div>
        </>
    )
}