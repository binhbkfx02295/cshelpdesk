export default function Report() {
    return (
        <>
        { /* <!--    report content--> */ }
    <div className="report page-main-content g-0 d-flex flex-column ">
        <div className="page-list">
            <div className="page-list-body container-fluid">
                <div className="row">
                    { /* <!-- chart item--> */ }
                    <div className="col-6">
                        <div className="chart-item" id="ticketByHour">

                            <div
                                    className="chart-header d-flex flex-row justify-content-start align-items-center p-3">
                                { /* <!-- //header --> */ }
                                <div className="chart-title d-flex align-items-center gap-2 me-auto">
                                    <i className="bi bi-graph-up-arrow fs-5 text-primary"></i>
                                    <div className="chart-title">Ticket Mỗi Giờ</div>
                                </div>

                                <div className="field-group dateOption" data-dateoption="1w" data-label="1 Tuần">
                                    1W
                                </div>
                                <div className="field-group dateOption" data-dateoption="1m" data-label="1 Tháng">
                                    1M
                                </div>
                                <div className="field-group dateOption" data-dateoption="3m" data-label="3 Tháng">
                                    3M
                                </div>

                                { /* <!-- date range option --> */ }
                                <div className="field-group dateRange">
                                    { /* <!-- <input type="text" id="dateRange" placeholder="Chọn ngày..." /> --> */ }
                                    <div className="flatpick"><i
                                            className="bi bi-calendar"></i></div>
                                </div>

                                { /* <!-- add dataset --> */ }
                                <div className="field-group datasets">
                                    <div className="dropdown">
                                        <a className="" type="button" data-bs-toggle="dropdown"
                                           aria-expanded="false">
                                            <i className="bi bi-three-dots-vertical"></i>
                                        </a>
                                        <ul className="dropdown-menu">
                                            <li><a className="dropdown-item add-dataset" href="#">Thêm
                                                dữ liệu</a></li>
                                            <div className="dropdown-divider"></div>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                            <div className="chart-metrics container-fluid">
                                { /* <!-- //metrics --> */ }
                                <div className="row mb-3 g-3">
                                    <div className="col-md-4">
                                        <div className="widget stat-card text-center">

                                            <div className="stat-title"><i
                                                    className="bi bi-bar-chart-fill stat-icon me-2"></i>Trung
                                                bình</div>
                                            <div className="stat-value avgTicketPerHour">- -</div>
                                            <div className="stat-label">Ticket / Giờ</div>
                                        </div>
                                    </div>

                                    <div className="col-md-4">
                                        <div className="widget widget-sm stat-card text-center">
                                            <div className="stat-title"><i
                                                    className="bi bi-arrow-up-right-circle-fill stat-icon me-2"></i>Nhiều
                                                nhất</div>
                                            <div className="stat-value maxTicketPerHour">- -</div>
                                            <div className="stat-label">Ticket / Giờ</div>
                                        </div>
                                    </div>

                                    <div className="col-md-4">
                                        <div className="widget widget-sm stat-card text-center">
                                            <div className="stat-title"><i
                                                    className="bi bi-arrow-down-right-circle-fill me-2 stat-icon"></i>
                                                Thấp nhất</div>
                                            <div className="stat-value minTicketPerHour">- -</div>
                                            <div className="stat-label">Ticket / Giờ</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="chart-body">
                                <canvas width="993" height="330"
                                        style={{ display: 'block', boxSizing: 'border-box', height: '300px', width: '903px' }}>
                                </canvas>
                            </div>
                            <div className="chart-tabular-data data-table container-fluid">
                                <div className="wrapper">
                                    <div className="data-header"></div>
                                    <div className="data-body"></div>
                                </div>
                            </div>

                        </div>
                    </div>
                    { /* <!-- chart item--> */ }
                    <div className="col-6">
                        <div className="chart-item" id="ticketByWeekday">

                            <div
                                    className="chart-header d-flex flex-row justify-content-start align-items-center p-3">
                                { /* <!-- //header --> */ }
                                <div className="chart-title d-flex align-items-center gap-2 me-auto">
                                    <i className="bi bi-graph-up-arrow fs-5 text-primary"></i>
                                    <div className="chart-title">Ticket Mỗi Tuần</div>
                                </div>

                                <div className="field-group dateOption" data-dateoption="1w" data-label="1 Tuần">
                                    1W
                                </div>
                                <div className="field-group dateOption" data-dateoption="4w" data-label="4 Tuần">
                                    4W
                                </div>
                                <div className="field-group dateOption" data-dateoption="12w" data-label="12 Tuần">
                                    12W
                                </div>

                                { /* <!-- date range option --> */ }
                                <div className="field-group dateRange">
                                    { /* <!-- <input type="text" id="dateRange" placeholder="Chọn ngày..." /> --> */ }
                                    <div className="flatpick"><i className="bi bi-calendar"></i></div>
                                </div>

                                { /* <!-- add dataset --> */ }
                                <div className="field-group datasets">
                                    <div className="dropdown">
                                        <a className="" type="button" data-bs-toggle="dropdown"
                                           aria-expanded="false">
                                            <i className="bi bi-three-dots-vertical"></i>
                                        </a>
                                        <ul className="dropdown-menu">
                                            <li><a className="dropdown-item add-dataset" href="#">Thêm
                                                dữ liệu</a></li>
                                            <div className="dropdown-divider"></div>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                            <div className="chart-metrics container-fluid">
                                { /* <!-- //metrics --> */ }
                                <div className="row mb-3 g-3">
                                    <div className="col-md-4">
                                        <div className="widget stat-card text-center">

                                            <div className="stat-title"><i
                                                    className="bi bi-bar-chart-fill stat-icon me-2"></i>Trung
                                                bình</div>
                                            <div className="stat-value avgTicketPerHour">- -</div>
                                            <div className="stat-label">Ticket / Ngày</div>
                                        </div>
                                    </div>

                                    <div className="col-md-4">
                                        <div className="widget widget-sm stat-card text-center">
                                            <div className="stat-title"><i
                                                    className="bi bi-arrow-up-right-circle-fill stat-icon me-2"></i>Nhiều
                                                nhất</div>
                                            <div className="stat-value maxTicketPerHour">- -</div>
                                            <div className="stat-label">Ticket / Ngày</div>
                                        </div>
                                    </div>

                                    <div className="col-md-4">
                                        <div className="widget widget-sm stat-card text-center">
                                            <div className="stat-title"><i
                                                    className="bi bi-arrow-down-right-circle-fill me-2 stat-icon"></i>
                                                Thấp nhất</div>
                                            <div className="stat-value minTicketPerHour">- -</div>
                                            <div className="stat-label">Ticket / Ngày</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="chart-body">
                                <canvas width="993" height="330"
                                        style={{ display: 'block', boxSizing: 'border-box', height: '300px', width: '903px' }}>
                                </canvas>
                            </div>
                            <div className="chart-tabular-data data-table container-fluid">
                                <div className="wrapper">
                                    <div className="data-header"></div>
                                    <div className="data-body"></div>
                                </div>
                            </div>

                        </div>
                    </div>
                    { /* <!-- chart item--> */ }
                    <div className="col-12">
                        <div className="chart-item" id="ticketByDay">
                            <div
                                    className="chart-header d-flex flex-row justify-content-start align-items-center p-3">
                                { /* <!-- //header --> */ }
                                <div className="chart-title d-flex align-items-center gap-2 me-auto">
                                    <i className="bi bi-graph-up-arrow fs-5 text-primary"></i>
                                    <div className="chart-title">Ticket Mỗi Ngày</div>
                                </div>

                                <div className="field-group dateOption" data-dateoption="1m" data-label="1 Tháng">
                                    1M
                                </div>
                                <div className="field-group dateOption" data-dateoption="3m" data-label="3 Tháng">
                                    3M
                                </div>
                                <div className="field-group dateOption" data-dateoption="6m" data-label="6 Tháng">
                                    6M
                                </div>

                                { /* <!-- date range option --> */ }
                                <div className="field-group dateRange">
                                    { /* <!-- <input type="text" id="dateRange" placeholder="Chọn ngày..." /> --> */ }
                                    <div className="flatpick"><i className="bi bi-calendar"></i></div>
                                </div>

                                { /* <!-- add dataset --> */ }
                                <div className="field-group datasets">
                                    <div className="dropdown">
                                        <a className="" type="button" data-bs-toggle="dropdown"
                                           aria-expanded="false">
                                            <i className="bi bi-three-dots-vertical"></i>
                                        </a>
                                        <ul className="dropdown-menu">
                                            <li><a className="dropdown-item add-dataset" href="#">Thêm
                                                dữ liệu</a></li>
                                            <div className="dropdown-divider"></div>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                            <div className="chart-metrics container-fluid">
                                { /* <!-- //metrics --> */ }
                                <div className="row mb-3 g-3">
                                    <div className="col-md-4">
                                        <div className="widget stat-card text-center">

                                            <div className="stat-title"><i
                                                    className="bi bi-bar-chart-fill stat-icon me-2"></i>Trung
                                                bình</div>
                                            <div className="stat-value avgTicketPerHour">- -</div>
                                            <div className="stat-label">Ticket / Ngày</div>
                                        </div>
                                    </div>

                                    <div className="col-md-4">
                                        <div className="widget widget-sm stat-card text-center">
                                            <div className="stat-title"><i
                                                    className="bi bi-arrow-up-right-circle-fill stat-icon me-2"></i>Nhiều
                                                nhất</div>
                                            <div className="stat-value maxTicketPerHour">- -</div>
                                            <div className="stat-label">Ticket / Ngày</div>
                                        </div>
                                    </div>

                                    <div className="col-md-4">
                                        <div className="widget widget-sm stat-card text-center">
                                            <div className="stat-title"><i
                                                    className="bi bi-arrow-down-right-circle-fill me-2 stat-icon"></i>
                                                Thấp nhất</div>
                                            <div className="stat-value minTicketPerHour">- -</div>
                                            <div className="stat-label">Ticket / Ngày</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="chart-body">
                                <canvas width="993" height="330"
                                        style={{ display: 'block', boxSizing: 'border-box', height: '300px', width: '903px' }}>
                                </canvas>
                            </div>
                            <div className="chart-tabular-data data-table container-fluid">
                                <div className="wrapper">
                                    <div className="data-header"></div>
                                    <div className="data-body"></div>
                                </div>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>
        { /* <!-- template cho cai add dataset --> */ }
        <template className="template-add-dataset">
            { /* <!-- floating div --> */ }
            <div className="dataset d-flex -flex-row align-items-center bg-white position-absolute movable">
                { /* <!-- dataset-name --> */ }
                <div className="field-group">
                    <input type="text" className="dataset-name" placeholder="Tên dữ liệu" />
                </div>
                { /* <!-- pick date range --> */ }
                <div className="field-group date-range ms-auto">
                    { /* <!-- <label for="" className="d-inline-block fw-normal me-2">Thời gian:
                                </label> --> */ }
                    <select className="form-select d-inline-block" id="dateRangeOption"
                            aria-label="Default select example">
                        <option value="1" selected>Hôm nay</option>
                        <option value="2">Tuần này</option>
                        <option value="3">Tuần trước</option>
                        <option value="4">4 tuần</option>
                        <option value="5">Tháng này</option>
                        <option value="6">Tháng trước</option>
                        <option value="7">3 tháng</option>
                    </select>
                </div>
                { /* <!-- pick chart type --> */ }
                <div className="field-group chart-type" data-value="bar">
                    <div className="dropdown">
                        <a className="" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                            <i className="bi bi-bar-chart me-2"></i>
                        </a>
                        <ul className="dropdown-menu">
                            <li><a className="dropdown-item" href="#" data-value="bar"><i
                                    className="bi bi-bar-chart me-2"></i>Biểu đồ
                                cột</a>
                            </li>
                            <li><a className="dropdown-item" href="#" data-value="line"><i
                                    className="bi bi-graph-up me-2"></i>Biểu đồ
                                đường</a>
                            </li>
                        </ul>
                    </div>
                </div>
                { /* <!-- confirm add dataset --> */ }
                <div className="field-group">
                    <i className="bi bi-check-lg confirm-add"></i>
                </div>
                { /* <!-- refresh chart --> */ }
                <div className="field-group">
                    <i className="bi bi-arrow-counterclockwise refresh-dataset"></i>
                </div>

                { /* <!-- cancel add --> */ }
                <div className="field-group">
                    <i className="bi bi-x-circle cancel-add"></i>
                </div>
                <div className="field-group ms-2 border-start">
                    <i className="bi bi-arrows-move move-trigger"></i>
                </div>
            </div>
        </template>
    </div>
        </>
    )
}