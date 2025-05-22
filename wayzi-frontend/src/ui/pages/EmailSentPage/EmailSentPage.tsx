import {useLocation} from "react-router-dom";
import {useNavigate} from "react-router";

export const EmailSentPage = () => {

    const navigate = useNavigate();
    const location = useLocation();
    const email = location.state?.email;

    return (
        <div className="page-wrapper">
            <div className="container py-5">
                <div className="row justify-content-center">
                    <div className="col-md-6">
                        <div className="bg-white p-5 rounded shadow-sm">
                            <img src="/assets/images/email-verification.jpg" alt="" width="350" height="350"
                                 className="rounded mx-auto d-block"/>

                            <div
                                className="d-flex flex-column align-items-center justify-content-center text-center mt-5 gap-3">
                                <h2 className="fw-bold text-dark-emphasis">
                                    Verify your email
                                </h2>
                                <p>
                                    We've sent an email to {email} to verify your email address and activate your account. The link in the email will expire in 30 minutes.
                                </p>
                            </div>

                            <p className="mt-3 text-center">
                                Already verified? <a className="fw-bold text-dark cursor-pointer" onClick={() => navigate("/login")}>Sign in</a>
                            </p>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    )

}