export const NoRides = () => {
    return (
        <div className="row justify-content-center mt-5">
            <div className="col-md-8">
                <img src="/assets/images/rides-not-found.jpg" alt="" height="350"
                     className="rounded mx-auto d-block"/>

                <div className="d-flex flex-column align-items-center justify-content-center text-center mt-5">
                    <h2 className="fw-bold text-dark-emphasis">
                        No rides found for the selected criteria. Please check again later!
                    </h2>
                </div>
            </div>
        </div>
    )
}