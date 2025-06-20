import {useDispatch, useSelector} from "react-redux";
import {AppDispatch, RootState} from "../../../redux/store.ts";
import {useEffect, useState} from "react";
import {fetchLocations} from "../../../redux/slices/locationSlice.ts";
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {useNavigate, useParams} from "react-router";
import {BookRideSchema, BookRideSchemaType} from "../../../schemas/bookRideSchema.ts";
import {paymentMethods} from "../../../constants/paymentMethods.ts";
import {clearCreateRideBookingError, createRideBooking} from "../../../redux/slices/rideBookingSlice.ts";



export const BookRideForm = () => {
    const {ride_id} = useParams()

    const navigate = useNavigate();
    const dispatch = useDispatch<AppDispatch>();
    const {createRideBookingError, createRideBookingLoading} = useSelector((state: RootState) => state.rideBookings)
    const [selectedPaymentMethod, setSelectedPaymentMethod] = useState<string | null>(null);

    const {register, control, handleSubmit, setValue, reset, formState: {errors}} = useForm<BookRideSchemaType>({
        resolver: zodResolver(BookRideSchema),
    });

    useEffect(() => {
        dispatch(clearCreateRideBookingError());

        return () => {
            dispatch(clearCreateRideBookingError());
        };
    }, [dispatch]);


    const locations = useSelector((state: RootState) => state.location.locations)

    useEffect(() => {
        if (locations.length == 0) {
            dispatch(fetchLocations());
        }

    }, [dispatch]);



    const onSubmit = async (data: BookRideSchemaType) => {
        const resultAction = await dispatch(createRideBooking({ id: String(ride_id), data }));

        if(createRideBooking.fulfilled.match(resultAction)) {
            navigate("/rides/" + ride_id)
            reset();
        }
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
                    </div>
                </div>

                <div className="modal-buttons d-flex justify-content-end gap-2">
                    <div className="cancel-btn">Cancel</div>
                    <button className="submit-btn" disabled={createRideBookingLoading}>Submit</button>
                </div>

                {createRideBookingError && (
                    <div className="alert alert-danger mt-4 border-0" role="alert">
                        {createRideBookingError}
                    </div>
                )}
            </form>


        </div>
    )
}