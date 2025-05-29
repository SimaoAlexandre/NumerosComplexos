package project;

import java.util.Arrays;

/**
 * @author Simão Alexandre 61874
 */

public class PolinomioVetor implements Polinomio {

	private Complexo[] coefs;

	/**
	 * Constrói um novo polinómio com os coeficientes dados.
	 * 
	 * @param coefs vetor do tipo complexo com os coeficientes do polinomio
	 */
	public PolinomioVetor(Complexo[] coefs) {
		this.coefs = Arrays.copyOf(coefs, coefs.length);
	}

	@Override
	public int grau() {

		return this.coefs.length - 1;
	}

	@Override
	public Complexo coef(int i) {

		return this.coefs[i];
	}

	@Override
	public boolean ehZero() {

		for (Complexo c : this.coefs) {
			if (c.ehZero())
				return true;
		}
		return false;
	}

	@Override
	public boolean ehZero(double erro) {

		for (Complexo c : this.coefs) {
			if (c.ehZero(erro))
				return true;
		}
		return false;
	}

	@Override
	public boolean ehConstante() {

		return grau() == 0;
	}

	@Override
	public Polinomio escalar(Complexo f) {
		Complexo[] v = new Complexo[this.coefs.length];
		for (int i = 0; i < v.length; i++) {
			v[i] = this.coefs[i].produto(f);
		}
		return new PolinomioVetor(v);
	}

	@Override
	public Polinomio simetrico() {

		Complexo[] simetricos = new Complexo[coefs.length];
		for (int i = 0; i < coefs.length; i++) {
			simetricos[i] = coefs[i].produto(new ComplexoConcreto(-1, 0));
		}
		return new PolinomioVetor(simetricos);
	}

	@Override
	public Polinomio soma(Polinomio p) {
		int grau = Math.max(this.grau(), p.grau());

		Complexo[] coefsSoma = new Complexo[grau + 1];

		for (int i = 0; i < grau + 1; i++) {
			if (i < this.coefs.length && i <= p.grau()) {
				Complexo coef1 = this.coef(i);
				Complexo coef2 = p.coef(i);
				coefsSoma[i] = coef1.soma(coef2);
			} else if (i > this.grau()) {
				coefsSoma[i] = p.coef(i);
			} else if (i > p.grau()) {
				coefsSoma[i] = coef(i);
			}
		}
		return new PolinomioVetor(coefsSoma);
	}

	@Override
	public Polinomio subtraccao(Polinomio p) {

		return soma(p.simetrico());
	}

	@Override
	public Polinomio produto(Polinomio p) { // Note que, se o polinómio não for constante, o último elemento do vetor
											// não pode ser 0.

		Complexo[] c = new Complexo[grau() + p.grau() + 1];
		for (int i = 0; i < c.length; i++) {
			c[i] = new ComplexoConcreto(0, 0);
		}
		for (int i = 0; i <= grau(); i++) {
			for (int j = 0; j <= p.grau(); j++) {
				c[i + j] = c[i + j].soma(coef(i).produto(p.coef(j)));
			}
		}
		int counter = 0;
		for (int k = c.length - 1; k > 0 && c[k].ehZero(); k--) {
			counter++;
		}
		return new PolinomioVetor(Arrays.copyOf(c, c.length - counter));
	}

	@Override
	public Complexo avalia(Complexo x) { // (3+4i)*(Complexo x)^3 + (-0.5i)*(Complexo x)^2 + ...

		Complexo c = new ComplexoConcreto(0, 0);
		for (int i = 0; i < this.coefs.length; i++) {
			c = c.soma(this.coef(i).produto(x.potencia(i)));
		}
		return c;
	}

	@Override
	public Polinomio derivada() { // (3+4i)* x^3 + (-0.5i)* x^2

		if (ehConstante()) { // 3' = 0
			Complexo[] coefsDerivada = { new ComplexoConcreto(0, 0) };
			return new PolinomioVetor(coefsDerivada);
		}

		Complexo[] coefsDerivada = new Complexo[this.grau()]; // nao é (grau + 1) pq o grau 0 se perde

		for (int i = 1; i <= this.grau(); i++) {
			coefsDerivada[i - 1] = this.coef(i).produto(new ComplexoConcreto(i, 0)); // (x^3)' = 3 * x^2
		}
		return new PolinomioVetor(coefsDerivada);
	}

	@Override
	public Polinomio copia() {

		return new PolinomioVetor(Arrays.copyOf(coefs, coefs.length));
	}

	@Override
	public boolean ehIgual(Polinomio p) {

		for (int i = 0; i < this.coefs.length; i++) {
			if (!coef(i).ehIgual(p.coef(i)))
				return false;
		}
		return true;
	}

	@Override
	public boolean ehIgual(Polinomio p, double erro) {

		for (int i = 0; i < this.coefs.length; i++) {
			if (!coef(i).ehIgual(p.coef(i), erro))
				return false;
		}
		return true;
	}

	/**
	 * Devolve uma representação textual do polinómio da seguinte forma:cn x n + … +
	 * c1 x + c0, em que ci é a representação do coeficiente do termo xi na forma
	 * trigonométrica
	 */
	public String toString() {

		StringBuilder sb = new StringBuilder();
		if (grau() == 0 && coef(0).ehZero())
			return sb.append(coef(0)).toString();
		else {
			for (int i = 0; i <= grau(); i++) {
				if (!coef(i).ehZero()) {
					if (i != grau()) {
						if (i == 0)
							if (coef(i).norma() != 0)
								sb.append(" + " + coef(i).norma() + " cis (" + coef(i).theta() + ")");
							else
								sb.append(" + " + coef(i).norma() + " cis (" + coef(i).theta() + ")");
						else if (i == 1)
							sb.insert(0, " + " + coef(i).norma() + " cis (" + coef(i).theta() + ") x");
						else if (i != 1 && i != 0)
							sb.insert(0, " + " + coef(i).norma() + " cis (" + coef(i).theta() + ") x^" + i);
					} else {
						if (i == 0)
							if (coef(i).norma() != 0)
								sb.append(coef(i).norma() + " cis (" + coef(i).theta() + ")");
							else
								sb.append(coef(i).norma() + " cis (" + coef(i).theta() + ")");
						else if (i == 1)
							sb.insert(0, coef(i).norma() + " cis (" + coef(i).theta() + ") x");
						else if (i != 1 && i != 0)
							sb.insert(0, coef(i).norma() + " cis (" + coef(i).theta() + ") x^" + i);
					}
				}
			}
		}
		return sb.toString();
	}

}
