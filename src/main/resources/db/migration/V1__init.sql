CREATE TABLE IF NOT EXISTS cases (
  id BIGSERIAL PRIMARY KEY,
  status VARCHAR(50) NOT NULL,
  patient_alias VARCHAR(100),
  age_range VARCHAR(50),
  confidentiality_level VARCHAR(50),
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);
