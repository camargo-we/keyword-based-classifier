package similarity;

public enum SimilarityLevel{
	NONE {
		@Override
		public double getReferenceValue() {
			return 0d;
		}
	},
	MINIMAL {
		@Override
		public double getReferenceValue() { return 0.11; }
	},
	SLIGHTLY {
		@Override
		public double getReferenceValue() {
			return 0.23;
		}
	},
	MEDIUM {
		@Override
		public double getReferenceValue() {
			return 0.47;
		}
	},
	MODERATE {
		@Override
		public double getReferenceValue() {
			return 0.61;
		}
	},
	HIGH {
		@Override
		public double getReferenceValue() {
			return 0.71;
		}
	},
	VERY_HIGH {
		@Override
		public double getReferenceValue() {
			return 0.79;
		}
	},
	ALMOST_TOTAL {
		@Override
		public double getReferenceValue() {
			return 0.89;
		}
	},
	TOTAL {
		@Override
		public double getReferenceValue() {
			return 1d;
		}
	};

	public abstract double getReferenceValue();

}

