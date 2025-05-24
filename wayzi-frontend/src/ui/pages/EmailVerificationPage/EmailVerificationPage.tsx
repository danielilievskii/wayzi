import { useEffect, useState } from "react";
import {useLocation, useNavigate} from "react-router-dom";
import authRepository from "../../../repository/authRepository.ts";

export const EmailVerificationPage = () => {
    const navigate = useNavigate();

    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    const token = queryParams.get("token");

    const [status, setStatus] = useState("loading");
    const [errorMessage, setErrorMessage] = useState("");
    const [successMessage, setSuccessMessage] = useState("");

    useEffect(() => {
        const verifyEmail = async () => {
            authRepository.verifyEmail(token)
                .then(() => {
                    setStatus("success");
                    setSuccessMessage("Your email has been successfully verified. You can now sign in.");
                })
                .catch((error) => {
                    console.error("Verification failed:", error);
                    const status = error.response?.status;

                    if (status === 409) {
                        // Email already verified
                        setTimeout(() => {
                            setStatus("success");
                            setSuccessMessage("Your email is already verified. You can now sign in.");
                        }, 2000);
                    } else {
                        setTimeout(() => {
                            setStatus("error");
                            setErrorMessage(error.response?.data || "Verification failed.");
                        }, 2000);
                    }
                })

        };

        verifyEmail();
    }, [token]);

    return (
        <div className="page-wrapper">
            <div className="container py-5">
                <div className="row justify-content-center">
                    <div className="col-md-6">
                        <div className="bg-white p-5 rounded shadow-sm text-center">
                            {status === "loading" && (
                                <>
                                    <div className="spinner-border text-primary" role="status">
                                        <span className="visually-hidden">Loading...</span>
                                    </div>
                                    <p className="mt-3">Verifying your email...</p>
                                </>
                            )}

                            {status === "success" && (
                                <>
                                    <img
                                        src="/assets/images/verification/success-1.png"
                                        alt="Success"
                                        width="150"
                                        className="mb-4"
                                    />
                                    <h2 className="text-success fw-bold">Email Verified!</h2>
                                    <p className="mt-2">{successMessage}</p>
                                    <button
                                        onClick={() => navigate("/login")}
                                        className="btn btn-light mt-3"
                                    >
                                        Sign in
                                    </button>
                                </>
                            )}

                            {status === "error" && (
                                <>
                                    <img
                                        src="/assets/images/verification/failure-1.png"
                                        alt="Error"
                                        width="150"
                                        className="mb-4"
                                    />
                                    <h2 className="text-danger fw-bold">Verification Failed</h2>
                                    <p className="mt-2">{errorMessage}</p>
                                    {errorMessage.includes("expired") ? (
                                        <button
                                            onClick={() => navigate("/resend-verification")}
                                            className="btn btn-light mt-3"
                                        >
                                            Resend verification
                                        </button>
                                    ) : (
                                        <button
                                            onClick={() => navigate("/register")}
                                            className="btn btn-light mt-3"
                                        >
                                            Create a new account
                                        </button>
                                    )}
                                </>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};
