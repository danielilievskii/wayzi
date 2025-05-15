import {useDispatch, useSelector} from "react-redux";
import {AppDispatch, RootState} from "../../../../redux/store.ts";
import {useAsyncThunkHandler} from "../../../../hooks/useAsyncThunkHandler.ts";
import {useEffect,useState} from "react";
import {fetchLocations} from "../../../../redux/slices/locationSlice.ts";
import {useForm} from "react-hook-form";
import {filterSchema, FilterSchemaType} from "../../../../schemas/filterSchema.ts";
import {zodResolver} from "@hookform/resolvers/zod";
// import {fetchFilteredRides, setFilter, setPagination} from "../../redux/slices/rideSlice.ts";



export const RidesFilterForm = (props) => {
    const {state, action, setFilter, setPagination} = props

    const dispatch = useDispatch<AppDispatch>();

    const locations = useSelector((state: RootState) => state.location.locations)
    const {filter} = state;

    const [departureInput, setDepartureInput] = useState(filter?.departureLocationName);
    const [arrivalInput, setArrivalInput] = useState(filter?.arrivalLocationName);

    const [departureSuggestions, toggleDepartureSuggestions] = useState(false);
    const [arrivalSuggestions, toggleArrivalSuggestions] = useState(false);


    const {register, handleSubmit, setValue} = useForm<FilterSchemaType>({
        resolver: zodResolver(filterSchema),
    })

    const {handleThunk, loading, success, error} = useAsyncThunkHandler();

    useEffect(() => {
        if (locations.length == 0) {
            dispatch(fetchLocations());
            console.log(locations)
        }

    }, [dispatch]);

    const filterSuggestions = (query: string) =>
        locations.filter((location) =>
            location.displayName.toLowerCase().includes(query.toLowerCase())
        );

    const onSubmit = (data: FilterSchemaType) => {
        dispatch(setFilter({...data, departureLocationName: departureInput, arrivalLocationName: arrivalInput}));

        const defaultPagination = { pageNum: 1, pageSize: 10 }
        dispatch(setPagination(defaultPagination));

        handleThunk(dispatch, action, data, () => {})
    }


    return (
        <form onSubmit={handleSubmit(onSubmit)} className="bg-white shadow-sm p-3 rounded mb-4" method="GET"
              action="/rides">
            <div className="row g-2 align-items-end">
                {/* Departure */}
                <div className="col-md-3 position-relative">
                    <div className="input-group">
                        <input
                            type="text"
                            name="departureLocationName"
                            className="form-control"
                            placeholder="e.g. Skopje"
                            value={departureInput}
                            onFocus={() => {
                                toggleArrivalSuggestions(false);
                            }}
                            onChange={(e) => {
                                setDepartureInput(e.target.value);
                                setValue("departureLocationId", null);
                                toggleDepartureSuggestions(true)
                            }}
                            autoComplete="off"
                        />
                        <span className="input-group-text">
                            <i className="fa-solid fa-location-dot"></i>
                        </span>
                    </div>
                    {departureSuggestions && departureInput && (
                        <div className="list-group position-absolute w-100" style={{zIndex: 1000, marginTop: 10}}>
                            {filterSuggestions(departureInput).slice(0, 10).map((location) => (
                                <button
                                    type="button"
                                    key={location.id}
                                    className="list-group-item list-group-item-action"
                                    onClick={() => {
                                        setDepartureInput(location.displayName);
                                        setValue("departureLocationId", location.id as FilterSchemaType["departureLocationId"]);
                                        toggleDepartureSuggestions(false);
                                    }}
                                >
                                    {location.displayName}
                                </button>
                            ))}
                        </div>
                    )}
                </div>

                {/* Arrival */}
                <div className="col-md-3 position-relative">
                    <div className="input-group">
                        <input
                            type="text"
                            name="arrivalLocationName"
                            className="form-control"
                            placeholder="e.g. Ohrid"
                            value={arrivalInput}
                            onFocus={() => {
                                toggleDepartureSuggestions(false);
                            }}
                            onChange={(e) => {
                                setArrivalInput(e.target.value);
                                setValue("arrivalLocationId", null);
                                toggleArrivalSuggestions(true)
                            }}
                            autoComplete="off"
                        />
                        <span className="input-group-text">
                            <i className="fa-solid fa-location-dot"></i>
                        </span>
                    </div>
                    {arrivalSuggestions && arrivalInput && (
                        <div className="list-group position-absolute w-100" style={{zIndex: 1000, marginTop: 10}}>
                            {filterSuggestions(arrivalInput).slice(0, 10).map((location) => (
                                <button
                                    type="button"
                                    key={location.id}
                                    className="list-group-item list-group-item-action"
                                    onClick={() => {
                                        setArrivalInput(location.displayName);
                                        setValue("arrivalLocationId", location.id as FilterSchemaType["arrivalLocationId"])
                                        toggleArrivalSuggestions(false);
                                    }}
                                >
                                    {location.displayName}
                                </button>
                            ))}
                        </div>
                    )}
                </div>

                {/* Date */}
                <div className="col-md-2">
                    <div className="input-group">
                        <input
                            type="date"
                            id="datePicker"
                            onFocus={() => {
                                toggleArrivalSuggestions(false);
                                toggleDepartureSuggestions(false);
                            }}
                            defaultValue={filter?.date}
                            className="form-control"
                            min={new Date().toISOString().split('T')[0]}
                            {...register("date")}
                        />
                        <span
                            className="input-group-text"
                            onClick={() => (document.getElementById("datePicker") as HTMLInputElement)?.showPicker()}
                            style={{cursor: 'pointer'}}
                        >
                        <i className="fa-solid fa-calendar"></i>
                        </span>
                    </div>
                </div>

                {/* Passengers */}
                <div className="col-md-2">
                    <div className="input-group">
                        <input
                            type="number"
                            onFocus={() => {
                                toggleArrivalSuggestions(false);
                                toggleDepartureSuggestions(false);
                            }}
                            defaultValue={filter?.passengersNum}
                            className="form-control"
                            placeholder="1 Passenger"
                            min={1}
                            max={10}
                            {...register("passengersNum")}

                        />
                        <span className="input-group-text">
              <i className="fa-solid fa-users"></i>
            </span>
                    </div>
                </div>

                {/* Search Button */}
                <div className="col-md-2">
                    <button type="submit" className="btn custom-btn w-100">
                        Search
                    </button>
                </div>
            </div>
        </form>
    )
        ;
}