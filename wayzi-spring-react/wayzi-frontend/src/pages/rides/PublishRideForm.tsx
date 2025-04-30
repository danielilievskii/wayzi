import {useUser} from "../../context/UserContext.tsx";
import {useDispatch, useSelector} from "react-redux";
import {AppDispatch, RootState} from "../../redux/store.ts";
import {useEffect, useState} from "react";
import {fetchLocations} from "../../redux/slices/locationSlice.ts";
import {Controller, useFieldArray, useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {useAsyncThunkHandler} from "../../hooks/useAsyncThunkHandler.ts";
import {createRide} from "../../redux/slices/publishedRideSlice.ts";
import {useNavigate} from "react-router";
import {PublishRideSchema, PublishRideSchemaType} from "../../schemas/publishRideSchema.ts";
import {fetchVehicles} from "../../redux/slices/vehicleSlice.ts";
import {Autocomplete, TextField} from "@mui/material";
import {string} from "zod";


export const PublishRideForm = () => {
    const {currentUser} = useUser()

    const navigate = useNavigate();
    const dispatch = useDispatch<AppDispatch>();

    const locations = useSelector((state: RootState) => state.location.locations)
    const vehicles = useSelector((state: RootState) => state.vehicle.vehicles)

    const [departureInput, setDepartureInput] = useState<string>("");
    const [departureSuggestions, toggleDepartureSuggestions] = useState<boolean>(false);

    const [arrivalInput, setArrivalInput] = useState<string>("");
    const [arrivalSuggestions, toggleArrivalSuggestions] = useState<boolean>(false);

    const [stopInputs, setStopInputs] = useState<string[]>([]);
    const [stopSuggestions, toggleStopSuggestions] = useState<boolean[]>([]);


    const {register, control, handleSubmit, setValue, reset, formState: {errors}} = useForm<PublishRideSchemaType>({
        resolver: zodResolver(PublishRideSchema),
    });

    const {fields, append, remove} = useFieldArray({
        control,
        name: "rideStops"
    });

    const {handleThunk, loading, success, error} = useAsyncThunkHandler();

    useEffect(() => {
        if (locations.length == 0) {
            dispatch(fetchLocations());
        }

        if (vehicles.length == 0) {
            dispatch(fetchVehicles());
        }

    }, [dispatch]);

    useEffect(() => {
        if (vehicles.length > 0) {
            setValue("vehicleId", vehicles[0].id.toString());
        }
    }, [vehicles]);

    const filterSuggestions = (query: string) =>
        locations.filter((location) =>
            location.displayName.toLowerCase().includes(query.toLowerCase())
        );

    const onSubmit = (data: PublishRideSchemaType) => {
        handleThunk(dispatch, createRide, data, () => {
            navigate("/rides/published")
            reset();
        });
    };

    return (
        <div className="custom-container mt-5 page-wrapper">
            <form id="form" className="bg-white p-4 rounded position-relative" onSubmit={handleSubmit(onSubmit)}>

                {/*<Controller*/}
                {/*    name="departureLocationId"*/}
                {/*    control={control}*/}
                {/*    render={({ field }) => (*/}
                {/*        <Autocomplete*/}
                {/*            options={locations}*/}
                {/*            getOptionLabel={(option) => option.displayName}*/}
                {/*            isOptionEqualToValue={(option, value) => option.id === value?.id}*/}
                {/*            onChange={(_, value) => field.onChange(value?.id ?? null)}*/}
                {/*            renderInput={(params) => (*/}
                {/*                <TextField*/}
                {/*                    {...params}*/}
                {/*                    label="Departure Location"*/}
                {/*                    error={!!errors.departureLocationId}*/}
                {/*                    helperText={errors.departureLocationId?.message}*/}
                {/*                />*/}
                {/*            )}*/}
                {/*        />*/}
                {/*    )}*/}
                {/*/>*/}

                <div className="row">
                    <div className="col-md-6 mb-3">
                        <div className="position-relative">
                            <label htmlFor="leavingFrom" className="form-label">Leaving from:</label>
                            <input
                                type="text"
                                className="form-control"
                                placeholder="e.g. Skopje"
                                value={departureInput}
                                {...register("departureLocationName")}
                                onFocus={() => toggleArrivalSuggestions(false)}
                                onChange={(e) => {
                                    setDepartureInput(e.target.value);
                                    setValue("departureLocationId", "");
                                    toggleDepartureSuggestions(true);
                                }}
                                autoComplete="off"
                            />
                            {errors.departureLocationId && (
                                <p className="text-danger">{errors.departureLocationId.message}</p>
                            )}

                            {departureSuggestions && departureInput && (
                                <div
                                    className="list-group position-absolute w-100"
                                    style={{ zIndex: 1000, marginTop: 10 }}
                                >
                                    {filterSuggestions(departureInput).slice(0, 10).map((location) => (
                                        <button
                                            type="button"
                                            key={location.id}
                                            className="list-group-item list-group-item-action"
                                            onClick={() => {
                                                setDepartureInput(location.displayName);
                                                setValue("departureLocationId", String(location.id));
                                                toggleDepartureSuggestions(false);
                                            }}
                                        >
                                            {location.displayName}
                                        </button>
                                    ))}
                                </div>
                            )}
                        </div>
                    </div>






                    <div className="col-md-6 mb-3">
                        <label className="form-label">Departure time:</label>
                        <div className="input-group">
                            <input type="datetime-local"
                                   id="datePickerDeparture"
                                   defaultValue={new Date().toISOString().slice(0, 16)}
                                   className="form-control"
                                   {...register("departureTime")}
                            />
                            <span className="cursor-pointer input-group-text"
                                  onClick={() => {
                                      (document.getElementById('datePickerDeparture') as HTMLInputElement).showPicker()
                                  }}>
                                <i className="fa-solid fa-calendar"></i>
                            </span>
                        </div>
                        {errors.departureTime && <p className="text-danger">{errors.departureTime.message}</p>}
                    </div>
                </div>

                <div className="row">
                    <div className="col-md-6 mb-3 position-relative">
                        <div className="position-relative">
                            <label className="form-label">Going to:</label>
                            <div className="input-group">
                                <input
                                    type="text"
                                    className="form-control"
                                    placeholder="e.g. Ohrid"
                                    value={arrivalInput}
                                    {...register("arrivalLocationName")}

                                    onFocus={() => {
                                        toggleDepartureSuggestions(false);
                                    }}
                                    onChange={(e) => {
                                        setArrivalInput(e.target.value);
                                        setValue("arrivalLocationId", "");
                                        toggleArrivalSuggestions(true)
                                    }}
                                    autoComplete="off"
                                />
                            </div>
                            {errors.arrivalLocationId && <p className="text-danger">{errors.arrivalLocationId.message}</p>}

                            {arrivalSuggestions && arrivalInput && (
                                <div className="list-group position-absolute w-100" style={{zIndex: 1000, marginTop: 10}}>
                                    {filterSuggestions(arrivalInput).slice(0, 10).map((location) => (
                                        <button
                                            type="button"
                                            key={location.id}
                                            className="list-group-item list-group-item-action"
                                            onClick={() => {
                                                setArrivalInput(location.displayName);
                                                setValue("arrivalLocationId", String(location.id))
                                                toggleArrivalSuggestions(false);
                                            }}
                                        >
                                            {location.displayName}
                                        </button>
                                    ))}
                                </div>
                            )}
                        </div>


                    </div>

                    <div className="col-md-6 mb-3">
                        <label className="form-label">Arrival time:</label>
                        <div className="input-group">
                            <input type="datetime-local"
                                   id="datePickerArrival"
                                   className="form-control"
                                   defaultValue={new Date(Date.now() + 60 * 60 * 2000).toISOString().slice(0, 16)}
                                   {...register("arrivalTime")}
                            />
                            <span className="cursor-pointer input-group-text"
                                  onClick={() => {
                                      (document.getElementById('datePickerDeparture') as HTMLInputElement).showPicker()
                                  }}>
                                <i className="fa-solid fa-calendar"></i>
                            </span>
                        </div>
                        {errors.arrivalTime && <p className="text-danger">{errors.arrivalTime.message}</p>}
                    </div>
                </div>

                <div className="row">
                    <div className="col-md-4 mb-3">
                        <label className="form-label">Vehicle:</label>
                        <select className="form-select" aria-label="Select a vehicle" {...register("vehicleId")}>
                            {vehicles.map((vehicle) => (
                                <option key={vehicle.id} value={vehicle.id}>
                                    {vehicle.brand} {vehicle.model} ({vehicle.color})
                                </option>
                            ))}
                        </select>
                        {errors.vehicleId && <p className="text-danger">{errors.vehicleId.message}</p>}
                    </div>

                    <div className="col-md-4 mb-3">
                        <label className="form-label">Available seats:</label>
                        <div className="input-group">
                            <input type="number" className="form-control" {...register("availableSeats")} min="1"/>
                            <span className="input-group-text"><i className="fa-solid fa-users"></i></span>
                        </div>
                        {errors.availableSeats && <p className="text-danger">{errors.availableSeats.message}</p>}
                    </div>

                    <div className="col-md-4 mb-3">
                        <label className="form-label">Price per seat:</label>
                        <div className="input-group">
                            <input type="number" className="form-control" {...register("pricePerSeat")} min="0"/>
                            <span className="input-group-text"><strong>MKD</strong></span>
                        </div>
                        {errors.pricePerSeat && <p className="text-danger">{errors.pricePerSeat.message}</p>}
                    </div>
                </div>

                <div className="or-thing mb-3">
                    <span>Intermediate stops</span>
                </div>

                <div id="stopContainer">
                    {fields.map((field, index) => (
                        <div key={field.id} className="row g-2 d-flex  mb-2 position-relative">
                            <div  className="col-md-1 mt-3 align-self-center">
                                <p>No. <span>{index + 1}</span></p>
                            </div>
                            <div className="col-md-5 position-relative">
                                <label className="form-label">Stop location</label>
                                <div style={{ minHeight: "70px" }}>
                                    <input
                                        type="text"
                                        className="form-control"
                                        value={stopInputs[index] || ""}
                                        onFocus={() => {
                                            toggleStopSuggestions((prev) => {
                                                const copy = [...prev];
                                                copy[index] = true;
                                                return copy;
                                            });
                                        }}
                                        onChange={(e) => {
                                            const newValue = e.target.value;
                                            // update input
                                            setStopInputs((prev) => {
                                                const copy = [...prev];
                                                copy[index] = newValue;
                                                return copy;
                                            });

                                            // reset selected locationId if typing
                                            setValue(`rideStops.${index}.locationId`, "");
                                            toggleStopSuggestions((prev) => {
                                                const copy = [...prev];
                                                copy[index] = true;
                                                return copy;
                                            });
                                        }}
                                        autoComplete="off"
                                    />
                                    {errors.rideStops?.[index]?.locationName && (
                                        <p className="text-danger">{errors.rideStops[index].locationName?.message}</p>
                                    )}
                                </div>

                                {stopSuggestions[index] && stopInputs[index] && (
                                    <div className="list-group position-absolute w-100">
                                        {filterSuggestions(stopInputs[index]).slice(0, 5).map((location) => (
                                            <button
                                                type="button"
                                                key={location.id}
                                                className="list-group-item list-group-item-action"
                                                onClick={() => {
                                                    const updatedInputs = [...stopInputs];
                                                    updatedInputs[index] = location.displayName;
                                                    setStopInputs(updatedInputs);

                                                    const updatedShow = [...stopSuggestions];
                                                    updatedShow[index] = false;
                                                    toggleStopSuggestions(updatedShow);

                                                    setValue(`rideStops.${index}.locationName`, location.displayName);
                                                    setValue(`rideStops.${index}.locationId`, String(location.id));
                                                }}
                                            >
                                                {location.displayName}
                                            </button>
                                        ))}
                                    </div>
                                )}

                            </div>

                            <div className="col-md-5">
                                <label className="form-label">Stop time</label>

                                <div className="input-group">
                                    <input type="datetime-local"
                                            id={"datePickerStop" + field.id}
                                           className="form-control"
                                           {...register(`rideStops.${index}.stopTime`)}
                                    />
                                    <span className="cursor-pointer input-group-text"
                                          onClick={() => {
                                              (document.getElementById('datePickerStop' + field.id) as HTMLInputElement).showPicker()
                                          }}>
                                         <i className="fa-solid fa-calendar"></i>
                                    </span>
                                </div>
                                {errors.rideStops?.[index]?.stopTime && (
                                    <p className="text-danger">{errors.rideStops[index].stopTime?.message}</p>
                                )}
                            </div>

                            <div className="col-md-1 align-self-center">
                                <button
                                    type="button"
                                    className="btn custom-btn w-100"
                                    onClick={() => {
                                        remove(index);
                                        setStopInputs((prev) => prev.filter((_, i) => i !== index));
                                        toggleStopSuggestions((prev) => prev.filter((_, i) => i !== index));
                                    }}
                                >
                                    <i className="fa-solid fa-trash"></i>
                                </button>
                            </div>
                        </div>
                    ))}
                </div>


                <button
                    type="button"
                    className="btn btn-secondary"
                    onClick={() => {
                        append({
                            locationName: "",
                            locationId: "",
                            // stopTime: "",
                            stopTime: new Date(Date.now() + 60 * 60 * 1000).toISOString().slice(0, 16),
                        })

                        setStopInputs((prev) => [...prev], "")
                        toggleStopSuggestions((prev) => [...prev, false])
                    }}
                >
                    Add stop
                </button>


                <div className="d-flex justify-content-end">
                    <button type="submit" className="btn btn-light ml-2">Cancel</button>
                    <button type="submit" className="btn btn-primary ml-2">Save</button>
                </div>
            </form>
        </div>
    )
}