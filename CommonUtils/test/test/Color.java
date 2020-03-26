package test;

public enum Color {
	RED( 1 ), BLUE( 2 ) {
		@Override
		public String toString() {
			return "Color." + super.toString();
		}
	};

	private int value;

	Color( int value ) {
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return this.value;
	}

	public static void main( String[] args ) {
		System.out.println( Color.RED );
		System.out.println( Color.BLUE );
		System.out.println( Color.RED == Color.BLUE );
		System.out.println( Color.RED.getValue() );
	}
}
