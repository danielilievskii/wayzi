import {useDispatch, useSelector} from "react-redux";
import {AppDispatch, RootState} from "../../../redux/store.ts";
import {useEffect, useState} from "react";
import {fetchLocations} from "../../../redux/slices/locationSlice.ts";
import {useFieldArray, useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {useNavigate, useParams} from "react-router";
import { PublishRideSchemaType} from "../../../schemas/publishRideSchema.ts";
import {fetchVehicles} from "../../../redux/slices/vehicleSlice.ts";
import {
    clearEditRideError,
    Ride, updateRide,
} from "../../../redux/slices/rideSlice.ts";
import rideRepository from "../../../repository/rideRepository.ts";
import {EditRideSchema, EditRideSchemaType} from "../../../schemas/editRideSchema.ts";



export const EditRideForm = () => {
    const navigate = useNavigate();
    const dispatch = useDispatch<AppDispatch>();

    const {rideId} = useParams();
    const publishedRides = useSelector((state: RootState) => state.publishedRides.publishedRides);
    const existingRide = publishedRides.find(ride => ride.id === rideId);
    const [ride, setRide] = useState<Ride | null>(existingRide || null);

    const locations = useSelector((state: RootState) => state.location.locations)
    const vehicles = useSelector((state: RootState) => state.vehicle.vehicles)

    const [departureInput, setDepartureInput] = useState<string>("");
    const [departureSuggestions, toggleDepartureSuggestions] = useState<boolean>(false);

    const [arrivalInput, setArrivalInput] = useState<string>("");
    const [arrivalSuggestions, toggleArrivalSuggestions] = useState<boolean>(false);

    const [stopInputs, setStopInputs] = useState<string[]>([]);
    const [stopSuggestions, toggleStopSuggestions] = useState<boolean[]>([]);

    const {register, control, handleSubmit, setValue, reset, formState: {errors}} = useForm<EditRideSchemaType>({
        resolver: zodResolver(EditRideSchema),
    });

    const {fields, append, remove} = useFieldArray({
        control,
        name: "rideStops"
    });

    const {updateRideError, updateRideLoading} = useSelector((state: RootState) => state.rides)

    useEffect(() => {
        dispatch(clearEditRideError());

        return () => {
            dispatch(clearEditRideError());
        };
    }, [dispatch]);

    // Fetch ride if not found in store
    useEffect(() => {
        const fetchRide = async () => {
            rideRepository.findById(String(rideId))
                .then((response) => {
                    setRide(response.data);
                })
                .catch((error) => {
                    console.error("Failed to fetch ride.", error);
                });
        };

        if (!ride && rideId) fetchRide();
    }, [ride, rideId]);

    // Populate form when ride is loaded
    useEffect(() => {
        if (ride) {
            setDepartureInput(ride.departureLocation.displayName)
            setArrivalInput(ride.arrivalLocation.displayName)

            const inputs = ride.rideStops.map((stop) => stop.location.displayName);
            setStopInputs(inputs);

            const formData = {
                rideStops: ride.rideStops.map(stop => ({
                    id: String(stop.id),
                    locationName: stop.location.displayName,
                    locationId: String(stop.location.id),
                    stopAddress: stop.stopAddress,
                    stopTime: new Date(stop.stopTime).toISOString().slice(0, 16),
                })),
                departureLocationName: ride.departureLocation.name,
                departureLocationId: String(ride.departureLocation.id),
                departureAddress: ride.departureAddress,
                arrivalLocationName: ride.arrivalLocation.name,
                arrivalLocationId: String(ride.arrivalLocation.id),
                arrivalAddress: ride.arrivalAddress,
                departureTime: new Date(ride.departureTime).toISOString().slice(0, 16),
                arrivalTime: new Date(ride.arrivalTime).toISOString().slice(0, 16),
                vehicleId: String(ride.vehicle.id),
                availableSeats: ride.availableSeats,
                pricePerSeat: ride.pricePerSeat,
            };
            reset(formData);

            console.log(ride)
        }
    }, [ride, setValue]);


    useEffect(() => {
        if (locations.length == 0) {
            dispatch(fetchLocations());
        }

        if (vehicles.length == 0) {
            dispatch(fetchVehicles());
        }

    }, [dispatch]);


    const filterSuggestions = (query: string) =>
        locations.filter((location) =>
            location.displayName.toLowerCase().includes(query.toLowerCase())
        );

    const onSubmit  = async (data: EditRideSchemaType) => {

        const updatedStops = data.rideStops?.map((ride, idx) => ({
            ...ride,
            stopOrder: idx+1,
        }));

        const newData = {
            ...data,
            rideStops: updatedStops,
        };


        const resultAction = await dispatch(updateRide({id: rideId, data: newData}));

        if (updateRide.fulfilled.match(resultAction)) {
            navigate("/rides/published");
            reset();
        }
    };



    return (
        <div className="custom-container mt-5 page-wrapper">
            <form id="form" className="bg-white p-4 rounded position-relative" onSubmit={handleSubmit(onSubmit)}>
                <div className="row">
                    <div className="col-md-6">
                        <div className="row mb-3">
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

                        <div className="row mb-3">
                            <label className="form-label">Departure address:</label>
                            <div className="input-group">
                                <input type="text" className="form-control" {...register("departureAddress")} min="0"/>
                            </div>
                            {errors.departureAddress && <p className="text-danger">{errors.departureAddress.message}</p>}
                        </div>

                        <div className="row mb-3">
                            <label className="form-label">Departure time:</label>
                            <div className="input-group">
                                <input type="datetime-local"
                                       id="datePickerDeparture"
                                       min={new Date().toISOString().slice(0, 16)}
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

                    <div className="col-md-6">
                        <div className="row mb-3">
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

                        <div className="row mb-3">
                            <label className="form-label">Arrival address:</label>
                            <div className="input-group">
                                <input type="text" className="form-control" {...register("arrivalAddress")} min="0"/>
                            </div>
                            {errors.arrivalAddress && <p className="text-danger">{errors.arrivalAddress.message}</p>}
                        </div>

                        <div className="row mb-3">
                            <label className="form-label">Arrival time:</label>
                            <div className="input-group">
                                <input type="datetime-local"
                                       id="datePickerArrival"
                                       className="form-control"
                                       min={new Date().toISOString().slice(0, 16)}
                                       {...register("arrivalTime")}
                                />
                                <span className="cursor-pointer input-group-text"
                                      onClick={() => {
                                          (document.getElementById('datePickerArrival') as HTMLInputElement).showPicker()
                                      }}>
                                        <i className="fa-solid fa-calendar"></i>
                                    </span>
                            </div>
                            {errors.arrivalTime && <p className="text-danger">{errors.arrivalTime.message}</p>}
                        </div>

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
                        <div key={field.id} className="row g-2 d-flex position-relative">
                            <div  className="col-md-1 mt-3 align-self-center">
                                <p>No. <span>{index + 1}</span></p>
                                <input type="text" hidden {...register(`rideStops.${index}.id`)} />
                            </div>
                            <div className="col-md-4 position-relative">
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

                            <div className="col-md-3">
                                <label className="form-label">Stop address:</label>
                                <div className="input-group">
                                    <input type="text" className="form-control"  {...register(`rideStops.${index}.stopAddress`)} min="0"/>
                                </div>
                                {errors.rideStops?.[index]?.stopAddress && (
                                    <p className="text-danger">{errors.rideStops[index].stopAddress?.message}</p>
                                )}
                            </div>

                            <div className="col-md-3">
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
                            id: null,
                            locationName: "",
                            locationId: "",
                            stopAddress: "",
                            stopTime: new Date(Date.now() + 60 * 60 * 1000).toISOString().slice(0, 16),
                        })

                        setStopInputs((prev) => [...prev], "")
                        toggleStopSuggestions((prev) => [...prev, false])
                    }}
                >
                    Add stop
                </button>


                <div className="d-flex justify-content-end">
                    <button type="submit" className="btn btn-light ms-2">Cancel</button>
                    <button type="submit" disabled={updateRideLoading} className="btn custom-btn ms-2">Save</button>
                </div>

                {updateRideError && <div className="alert alert-danger border-0 mt-2" role="alert">{updateRideError}</div>}
            </form>


        </div>
    )
}