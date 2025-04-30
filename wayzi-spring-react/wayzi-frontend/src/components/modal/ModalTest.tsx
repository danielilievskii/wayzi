import "./Modal.css"
import {useEffect, useState} from "react";
import {Modal} from 'react-responsive-modal';
import {VehicleSchemaType} from "../../schemas/vehicleSchema.ts";
import {paymentMethods} from "../../constants/paymentMethods.ts";

export const ModalTest = () => {

    const [modal, setModal] = useState(false);

    const toggleModal = () => {
        setModal(!modal);
        console.log("Modal toggle modal");
    };

    const [paymentMethod, setPaymentMethod] = useState<string | null>(null);


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
                        <div className="row">
                            <div className="col-md-6">
                                <div className="mb-3">
                                    <label className="form-label">How many seats?</label>
                                    <input type="number"  className="form-control" />
                                    {/*{errors.capacity && <p className="text-danger">{errors.capacity.message}</p>}*/}
                                </div>

                                <div className="mb-3">
                                    <label className="form-label">Payment method</label>
                                    <div className="row">
                                        {paymentMethods.map(type => (
                                            <div key={type.name} className="col-12 col-sm-6">
                                                <button
                                                    type="button"
                                                    className={`radio-btn w-100 text-center ${paymentMethod === type.name ? "active" : ""}`}
                                                    onClick={() => {
                                                        setPaymentMethod(type.name);
                                                        // setValue("type", type.name as VehicleSchemaType["type"]);
                                                    }}
                                                >
                                                    <i className={`fa-solid ${type.icon}`}></i>
                                                    <h5 className="card-title">{type.label}</h5>
                                                </button>
                                            </div>
                                        ))}
                                    </div>
                                    {/*{errors.type && <p className="text-danger">{errors.type.message}</p>}*/}
                                </div>
                            </div>
                            <div className="col-md-6">
                                <div className="mb-3">
                                    <label className="form-label">Message to the driver</label>
                                    <textarea className="form-control" rows={6} placeholder={"Optional..."}></textarea>
                                </div>

                            </div>

                        </div>




                        <div className="modal-buttons d-flex justify-content-end gap-2">
                            <div className="cancel-btn" onClick={toggleModal}>Cancel</div>
                            <button className="submit-btn">Submit</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}