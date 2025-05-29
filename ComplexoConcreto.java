package project;

/**
 * @author Simão Alexandre 61874
 */

public class ComplexoConcreto implements Complexo {

	private final double[] partesDoComplexo = new double[2];

	/**
	 * Constrói um novo complexo dadas a parte real e a parte imaginária
	 * 
	 * @param re parte real do complexo
	 * @param im parte imaginária do complexo
	 */
	public ComplexoConcreto(double re, double im) {

		this.partesDoComplexo[0] = re;
		this.partesDoComplexo[1] = im;
	}

	@Override
	public double re() {

		return this.partesDoComplexo[0];
	}

	@Override
	public double im() {

		return this.partesDoComplexo[1];
	}

	@Override
	public double rho() {

		return Math.sqrt(this.re() * this.re() + this.im() * this.im());
	}

	@Override
	public double theta() {

		return Math.atan2(this.im(), this.re()); // Da relação tgq = b/a consegue-se o valor de q: é tal que tgq = b/a.
	}

	@Override
	public double norma() {

		return rho();
	}

	@Override
	public Complexo soma(Complexo outro) {

		double real = this.re() + outro.re();
		double imaginario = this.im() + outro.im();
		return new ComplexoConcreto(real, imaginario);
	}

	@Override
	public Complexo produto(Complexo outro) { // z1.z2 = (ac - bd) + (ad + cb)i z1 = a + bi z2 = c + di

		double real = this.re() * outro.re() - this.im() * outro.im();
		double imaginario = this.re() * outro.im() + this.im() * outro.re();
		return new ComplexoConcreto(real, imaginario);
	}

	@Override
	public Complexo potencia(double x) { // z^n = r^n * cis(n*t).

		double t = Math.atan2(this.im(), this.re());
		double novaNorma = Math.pow(norma(), x);
		double xt = x * t;
		double real = novaNorma * Math.cos(xt);
		double imaginario = novaNorma * Math.sin(xt);
		return new ComplexoConcreto(real, imaginario);
	}

	@Override
	public Complexo quociente(Complexo outro) { // a / b = (a * b.conjugado) / b * b.conjugado)

		double denominador = outro.re() * outro.re() + outro.im() * outro.im();
		Complexo c = this.produto(outro.conjugado());
		return new ComplexoConcreto(c.re() / denominador, c.im() / denominador);
	}

	@Override
	public Complexo conjugado() {

		return new ComplexoConcreto(this.re(), this.im() * -1);
	}

	@Override
	public boolean ehReal() { // Quando a parte imaginária é nula, o número complexo é também um número real.

		return this.im() <= Complexo.ERRO;
	}

	@Override
	public boolean ehReal(double erro) {

		return this.im() <= erro;
	}

	@Override
	public boolean ehZero() {

		return Math.abs(this.re()) <= Complexo.ERRO && Math.abs(this.im()) <= Complexo.ERRO;
	}

	@Override
	public boolean ehZero(double erro) {

		return Math.abs(this.re()) <= erro && Math.abs(this.im()) <= erro;
	}

	@Override
	public boolean ehIgual(Complexo outro) {

		return Math.abs(this.re() - outro.re()) <= Complexo.ERRO && Math.abs(this.im() - outro.im()) <= Complexo.ERRO;
	}

	@Override
	public boolean ehIgual(Complexo outro, double erro) {

		return Math.abs(this.re() - outro.re()) <= erro && Math.abs(this.im() - outro.im()) <= erro;
	}

	public String toString() {

		if (!ehZero()) {
			if (this.re() == 0 && this.im() < 0)
				return new String("- " + Math.abs(this.im()) + "i");
			else if (this.re() == 0 && this.im() > 0)
				return new String(Math.abs(this.im()) + "i");
			else if (this.re() < 0 && this.im() == 0)
				return new String("- " + Math.abs(this.re()));
			else if (this.re() > 0 && this.im() == 0)
				return new String(String.valueOf(Math.abs(this.re())));
			else if (this.re() < 0 && this.im() < 0)
				return new String("- " + Math.abs(this.re()) + " - " + Math.abs(this.im()) + "i");
			else if (this.re() < 0 && this.im() > 0)
				return new String("- " + Math.abs(this.re()) + " + " + Math.abs(this.im()) + "i");
			else if (this.re() > 0 && this.im() > 0)
				return new String(Math.abs(this.re()) + " + " + Math.abs(this.im()) + "i");
			else if (this.re() > 0 && this.im() < 0)
				return new String(Math.abs(this.re()) + " - " + Math.abs(this.im()) + "i");
		}
		return String.valueOf(re());

	}

	@Override
	public String repTrigonometrica() { // complexo na forma r cis (t)

		return new String(rho() + " cis (" + theta() + ")");
	}

}