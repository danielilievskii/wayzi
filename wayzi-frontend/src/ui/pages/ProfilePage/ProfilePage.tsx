
import {Vehicles} from "../../components/profile/Vehicles/Vehicles.tsx";
import {ProfilePictureCard} from "../../components/profile/ProfilePictureCard/ProfilePictureCard.tsx";

export const ProfilePage = () => {

    return (
        <div className="page-wrapper">
            <section className="custom-container mt-5">
                <div className="d-flex flex-column gap-3">

                    <ProfilePictureCard/>

                    <div className="floating-card shadow-sm p-3">
                        <h5>About you</h5>
                    </div>

                    {/* Profile Vehicles Section */}
                    <Vehicles/>
                </div>
            </section>
        </div>
    );
};
