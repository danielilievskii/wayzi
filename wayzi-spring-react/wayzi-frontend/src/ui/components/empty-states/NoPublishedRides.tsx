export const NoPublishedRides = () => {
    return (
        <div className="row justify-content-center">
            <div className="col-md-6">
                <img src="/assets/images/rides-published-2.jpg" alt="" height="350"
                     className="rounded mx-auto d-block"/>

                <div
                    className="d-flex flex-column align-items-center justify-content-center text-center mt-5">
                    <h2 className="fw-bold text-dark-emphasis">
                        You haven't published any rides yet.
                    </h2>
                    <p>
                        Start sharing your journey and find passengers to split travel costs.
                    </p>
                </div>
            </div>
        </div>
    )
}