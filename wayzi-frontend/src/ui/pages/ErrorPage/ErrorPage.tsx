import "./ErrorPage.css"

import {Link} from "react-router-dom";

export const ErrorPage = () => {
    return (
        <>
            <div className="d-flex align-items-center justify-content-center vh-100">
                <div className="col-md-6">
                    {/*<img src="/assets/images/error.jpg" alt="" height="350"*/}
                    {/*     className="rounded mx-auto d-block"/>*/}

                    <div className="d-flex flex-column align-items-center justify-content-center text-center mt-5">
                            <div className="text-center">
                                <h1 className="display-1 fw-bold">404</h1>
                                <p className="fs-3"><span className="text-danger">Opps!</span> Page not found.</p>
                                <p className="lead">
                                    The page you’re looking for is not available or doesn’t exist.
                                </p>
                                <div className="mt-3">
                                    <Link to="/" className="go-back-btn ">Go back</Link>
                                </div>
                            </div>
                    </div>
                </div>
            </div>



        </>


    )
}