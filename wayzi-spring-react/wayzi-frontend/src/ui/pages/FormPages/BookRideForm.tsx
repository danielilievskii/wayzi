import {useUser} from "../../../context/UserContext.tsx";
import {useDispatch, useSelector} from "react-redux";
import {AppDispatch, RootState} from "../../../redux/store.ts";
import {useEffect, useState} from "react";
import {fetchLocations} from "../../../redux/slices/locationSlice.ts";
import {Controller, useFieldArray, useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {useAsyncThunkHandler} from "../../../hooks/useAsyncThunkHandler.ts";
import {createRide} from "../../../redux/slices/publishedRideSlice.ts";
import {useNavigate, useParams} from "react-router";
import {BookRideSchema, BookRideSchemaType} from "../../../schemas/bookRideSchema.ts";
import {VehicleSchemaType} from "../../../schemas/vehicleSchema.ts";
import {paymentMethods} from "../../../constants/paymentMethods.ts";
import {createRideBooking, fetchRideBooking, fetchRideBookings} from "../../../redux/slices/rideBookingSlice.ts";


export const BookRideForm = () => {
    const {currentUser} = useUser()
    const {ride_id} = useParams()

    const navigate = useNavigate();
    const dispatch = useDispatch<AppDispatch>();

    const [selectedPaymentMethod, setSelectedPaymentMethod] = useState<string | null>(null);


    const locations = useSelector((state: RootState) => state.location.locations)
    const [stopInput, setDepartureInput] = useState<string>("");
    const [stopSuggestions, toggleDepartureSuggestions] = useState<boolean>(false);


    const {register, control, handleSubmit, setValue, reset, formState: {errors}} = useForm<BookRideSchemaType>({
        resolver: zodResolver(BookRideSchema),
    });


    const {handleThunk, loading, success, error} = useAsyncThunkHandler();

    useEffect(() => {
        if (locations.length == 0) {
            dispatch(fetchLocations());
        }

    }, [dispatch]);


    const onSubmit = (data: BookRideSchemaType) => {
        handleThunk(dispatch, createRideBooking, { id: ride_id, data }, () => {

            handleThunk(dispatch, fetchRideBookings, null, () => {
                navigate("/rides/bookings")
                reset();
            })

        });
    };

    return (
        <div className="custom-container mt-5 page-wrapper">
            <form id="form" className="bg-white mt-5 p-4 rounded position-relative" onSubmit={handleSubmit(onSubmit)}>
                <div className="row">
                    <div className="col-md-6">
                        <div className="mb-3">
                            <label className="form-label">How many seats?</label>
                            <input type="number" className="form-control" {...register("bookedSeats")}
                                   min="1"/>
                            {errors.bookedSeats &&
                                <p className="text-danger">{errors.bookedSeats.message}</p>}
                        </div>

                        <div className="mb-3">
                            <label className="form-label">Payment method</label>
                            <div className="row">
                                {paymentMethods.map(paymentMethod => (
                                    <div key={paymentMethod.name} className="col-12 col-sm-6">
                                        <button
                                            type="button"
                                            disabled={paymentMethod.name === "CARD"}
                                            className={`radio-btn w-100 text-center 
                                                ${selectedPaymentMethod === paymentMethod.name ? "active" : ""}
                                              
                                            `}
                                            onClick={() => {
                                                setSelectedPaymentMethod(paymentMethod.name);
                                                setValue("paymentMethod", paymentMethod.name as BookRideSchemaType["paymentMethod"]);
                                            }}
                                        >
                                            <i className={`fa-solid ${paymentMethod.icon}`}></i>
                                            <h5 className="card-title">{paymentMethod.label}</h5>
                                        </button>
                                    </div>
                                ))}
                            </div>
                            {errors.paymentMethod &&
                                <p className="text-danger">{errors.paymentMethod.message}</p>}
                        </div>
                    </div>
                    <div className="col-md-6">
                        <div className="mb-3">
                            <label className="form-label">Message to the driver</label>
                            <textarea
                                className="form-control"
                                rows={7}
                                placeholder={"Optional..."}
                                {...register("message")}>
                                        </textarea>
                        </div>
                        {errors.paymentMethod &&
                            <p className="text-danger">{errors.paymentMethod.message}</p>}

                    </div>
                </div>

                <div className="modal-buttons d-flex justify-content-end gap-2">
                    <div className="cancel-btn">Cancel</div>
                    <button className="submit-btn">Submit</button>
                </div>

                {error && (
                    <div className="alert alert-danger mt-4 border-0" role="alert">
                        {error}
                    </div>
                )}
            </form>


        </div>
    )
}