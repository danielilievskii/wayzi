import React from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {AppDispatch, RootState} from '../redux/store';
import {PaginationSchemaType} from "../schemas/paginationSchema.ts";
import {RidesState} from "../redux/slices/rideSlice.ts";
// import {setPagination} from "../redux/slices/rideSlice.ts";
import "../styles/global.css"

interface Props {
    onPageChange: (pagination: PaginationSchemaType) => void;
    setPagination: any;
    state: any;
}

export const RidesPagination = ({ onPageChange, setPagination, state }: Props) => {  // Destructure onPageChange
    const dispatch = useDispatch<AppDispatch>();

    const { totalPages, currentPage, totalItems, pagination } = state

    const page = currentPage;
    const isFirst = page === 0;
    const isLast = page === totalPages - 1;

    const getPageNumbers = () => {
        const range = [];
        for (let i = 1; i <= totalPages; i++) {
            range.push(i);
        }
        return range;
    };

    const handleChangePage = (newPage: number) => {
        const newPagination =  { pageNum: newPage, pageSize: pagination?.pageSize }

        dispatch(setPagination(newPagination));
        onPageChange(newPagination);
    };

    const handleChangePageSize = (e: React.ChangeEvent<HTMLSelectElement>) => {
        const newSize = parseInt(e.target.value);
        const newPagination = { pageNum: 1, pageSize: newSize }

        dispatch(setPagination(newPagination));
        onPageChange(newPagination);
    };

    const startItem = pagination.pageSize * page + 1;
    const endItem = Math.min(startItem + pagination.pageSize - 1, totalItems);

    return (
        <div className="bg-white rounded px-4 py-2 d-flex align-items-center mt-3">
            <div className="col-7">
                <nav aria-label="Page navigation">
                    <ul className="pagination m-0">
                        <li className={`cursor-pointer page-item ${isFirst ? 'disabled' : ''}`}>
                            <a className="page-link" onClick={() => handleChangePage(1)}>
                                <i className="fa-solid fa-chevron-left"></i><i className="fa-solid fa-chevron-left"></i>
                            </a>
                        </li>
                        <li className={`cursor-pointer page-item ${page === 0 ? 'disabled' : ''}`}>
                            <a className="page-link" onClick={() => handleChangePage(page)}>
                                <i className="fa-solid fa-chevron-left"></i>
                            </a>
                        </li>

                        {getPageNumbers().map((pageNum) => (
                            <li key={pageNum} className="cursor-pointer">
                                <a
                                    className={`page-link fw-semibold`}
                                    style={{
                                        backgroundColor: page + 1 === pageNum ? 'cornflowerblue' : 'transparent',
                                        color: page + 1 === pageNum ? 'white' : 'black'
                                    }}
                                    onClick={() => handleChangePage(pageNum)}
                                >
                                    {pageNum}
                                </a>
                            </li>
                        ))}

                        <li className={`cursor-pointer page-item ${isLast ? 'disabled' : ''}`}>
                            <a className="page-link" onClick={() => handleChangePage(page + 2)}>
                                <i className="fa-solid fa-chevron-right"></i>
                            </a>
                        </li>
                        <li className={`cursor-pointer page-item ${isLast ? 'disabled' : ''}`}>
                            <a className="page-link" onClick={() => handleChangePage(totalPages)}>
                                <i className="fa-solid fa-chevron-right"></i><i className="fa-solid fa-chevron-right"></i>
                            </a>
                        </li>
                    </ul>
                </nav>
            </div>

            <div className="col-5 d-flex align-items-center justify-content-end gap-2 text-secondary">
                <label htmlFor="results" className="mb-0 me-2">Results per page:</label>
                <select
                    id="results"
                    className="form-control custom-select"
                    style={{ maxWidth: '100px' }}
                    value={pagination.pageSize || 10}
                    onChange={handleChangePageSize}
                >
                    <option value={1}>1</option>
                    <option value={2}>2</option>
                    <option value={10}>10</option>
                    <option value={20}>20</option>
                    <option value={50}>50</option>
                </select>
                <span>{startItem}-{endItem} of {totalItems} rides</span>
            </div>
        </div>
    );
};

