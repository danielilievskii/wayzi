import {useState} from "react";
import {Button, Modal} from "@mui/material";

export const QrCodeBtnDialog = (props) => {
    const {qrCodeUrl} = props;

    const [modal, setModal] = useState(false);

    const toggleModal = () => {
        setModal(!modal);
    };

    return (
        <>
            <button
                className={`btn btn-light p-2 fw-bold w-100`} onClick={toggleModal}>Show QR Code
            </button>
            <Modal
                open={modal}
                onClose={toggleModal}
                aria-labelledby="modal-modal-title"
                aria-describedby="modal-modal-description"
            >
                <div className="d-flex justify-content-center align-items-center vh-100">
                    <div className="bg-white p-4 rounded position-relative">
                        <button
                            type="button"
                            className="btn-close position-absolute top-0 end-0 m-2"
                            aria-label="Close"
                            onClick={toggleModal}
                        ></button>

                        <img
                            src={qrCodeUrl}
                            alt="QR Code"
                            className="img-fluid d-block mx-auto"
                            width={500}
                            height={500}
                        />
                    </div>
                </div>
            </Modal>
        </>
    )
}