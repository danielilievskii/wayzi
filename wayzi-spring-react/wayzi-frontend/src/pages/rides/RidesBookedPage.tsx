import {useUser} from "../../context/UserContext.tsx";

export const RidesBookedPage = () => {
    const { currentUser } = useUser()

    return (
        <>
            <section className="page-wrapper">
                <div className="container mt-4">
                    <div className="row justify-content-center">
                        <div className="col-md-6">
                            <img src="/assets/rides-booked-2.jpg" alt="" width="350" height="350"
                                 className="rounded mx-auto d-block"/>

                            <div
                                className="d-flex flex-column align-items-center justify-content-center text-center mt-5">
                                <h2 className="fw-bold text-dark-emphasis">
                                    Your future travel bookings will appear here.
                                </h2>
                                <p>
                                    Find the perfect ride from thousands of destinations to share your travel costs.
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

        </>
    )
}