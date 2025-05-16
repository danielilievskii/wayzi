import {useState} from "react";
import {Button, Modal} from "@mui/material";

export const QrCodeImageDialog = (props) => {
    const {qrCodeUrl} = props;

    const [modal, setModal] = useState(false);

    const toggleModal = () => {
        setModal(!modal);
    };

    return (
        <>
            <img
                src={qrCodeUrl}
                alt="QR Code"
                width="100"
                height="100"
                className="rounded bg-light p-1 cursor-zoom-in"
                onClick={toggleModal}
            />
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

                        <h5 className="text-dark text-center">Show this QR code at check-in</h5>
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