import styles from "./style.module.css";
import Link from "next/link"; 

const NavHeader: React.FC= () => {

    return(
        <nav className={styles.navContainer}>
        <div className={styles.navBarInner}>
            <div className={styles.navBarLogo}>
                <Link className={styles.link} href="/">TYLE</Link>
            </div>
            <div className={styles.navBarMenu}>
                <Link className={styles.link} href="/login">Account</Link>
                <Link className={styles.link} href="/rank">Rank</Link>
                <Link className={styles.link} href="/profile">Profile</Link>
            </div>
        </div>
        </nav>
    );
};
export default NavHeader;
