import "./Modal.css"
import {useEffect, useState} from "react";
import {Modal} from 'react-responsive-modal';
import {VehicleSchemaType} from "../../../schemas/vehicleSchema.ts";
import {paymentMethods} from "../../../constants/paymentMethods.ts";
import {useDispatch, useSelector} from "react-redux";
import {AppDispatch, RootState} from "../../../redux/store.ts";
import {string} from "zod";
import {useForm} from "react-hook-form";
import {BookRideSchema, BookRideSchemaType} from "../../../schemas/bookRideSchema.ts";
import {zodResolver} from "@hookform/resolvers/zod";
import {useAsyncThunkHandler} from "../../../hooks/useAsyncThunkHandler.ts";
import {fetchLocations} from "../../../redux/slices/locationSlice.ts";
import {useNavigate} from "react-router";
import {createRide} from "../../../redux/slices/publishedRideSlice.ts";

export const BookRideModal = () => {

    const [modal, setModal] = useState(false);

    const navigate = useNavigate();
    const dispatch = useDispatch<AppDispatch>();


    const toggleModal = () => {
        setModal(!modal);
    };

    const [selectedPaymentMethod, setSelectedPaymentMethod] = useState<string | null>(null);

    const locations = useSelector((state: RootState) => state.location.locations)
    const [stopInput, setDepartureInput] = useState<string>("");
    const [stopSuggestions, toggleDepartureSuggestions] = useState<boolean>(false);


    useEffect(() => {
        if (locations.length == 0) {
            dispatch(fetchLocations());
        }

    }, [dispatch]);


    const {register, control, handleSubmit, setValue, reset, formState: {errors}} = useForm<BookRideSchemaType>({
        resolver: zodResolver(BookRideSchema),
    });

    const {handleThunk, loading, success, error} = useAsyncThunkHandler();

    const onSubmit = (data: BookRideSchemaType) => {
        handleThunk(dispatch, createRide, data, () => {
            navigate("/rides/booked")
            reset();
        });
    };


    return (
        <div className="modal-wrap">
            <button onClick={toggleModal} className="btn btn-lg btn-info text-light fw-bold">Book now</button>

            <div className={`row modal-overlay ${modal ? 'show' : ''}`}>
                <div className="react-responsive-modal-modal bg-white col-md-6">
                    <div className="head-modal">
                        <h2>Book a ride</h2>
                        <i className="fa-solid fa-x btn-close-modal" onClick={toggleModal}></i>
                    </div>

                    <div className="modal-content">
                        <form onSubmit={handleSubmit(onSubmit)}>
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
                                                        className={`radio-btn w-100 text-center ${selectedPaymentMethod === paymentMethod.name ? "active" : ""}`}
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
                                            rows={6}
                                            placeholder={"Optional..."}
                                            {...register("message")}>
                                        </textarea>
                                    </div>
                                    {errors.paymentMethod &&
                                        <p className="text-danger">{errors.paymentMethod.message}</p>}

                                </div>
                            </div>

                            <div className="modal-buttons d-flex justify-content-end gap-2">
                                <div className="cancel-btn" onClick={toggleModal}>Cancel</div>
                                <button className="submit-btn">Submit</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
}