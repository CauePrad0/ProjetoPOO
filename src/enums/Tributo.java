package enums;

public enum Tributo {
    IMPORTACAO(0.60),
    ICMS(0.17);

    private final double aliquota;

    Tributo(double aliquota) {
        this.aliquota = aliquota;
    }

    public double getAliquota() {
        return aliquota;
    }
}
