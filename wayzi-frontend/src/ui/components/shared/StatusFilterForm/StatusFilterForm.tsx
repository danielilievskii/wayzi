import "./StatusFilterForm.css"
import {useState} from "react";
import {useAsyncThunkHandler} from "../../../../hooks/useAsyncThunkHandler.ts";
import {useDispatch} from "react-redux";
import {AppDispatch} from "../../../../redux/store.ts";

export const StatusFilterForm = (props) => {

    const {filters, state, action, setFilter, setPagination} = props

    const {filter} = state;
    const [selectedFilter, setSelectedFilter] = useState(filter?.status || "ALL");

    const dispatch = useDispatch<AppDispatch>();
    const {handleThunk, loading, success, error} = useAsyncThunkHandler();

    const onFilterSwitch = (status) => {
        dispatch(setFilter({status}));

        const defaultPagination = { pageNum: 1, pageSize: 10 }
        dispatch(setPagination(defaultPagination));

        handleThunk(dispatch, action, status === "ALL" ? null : {status}, () => {})
    }

    return (

        <div className="application-filters d-inline-flex flex-row justify-content-start mb-3">
            {
                filters.map(filter => (
                    <span
                        key={filter.value}
                        className={selectedFilter === filter.value ? "selected" : ""}
                        onClick={() => {
                            setSelectedFilter(filter.value)
                            onFilterSwitch(filter.value)
                        }}
                    >
                        {/*<i className={`fa-solid ${filter.icon}`}></i>*/}
                        {filter.label}
                    </span>
                ))
            }
        </div>
    )
}