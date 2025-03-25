const express = require("express");
const sqlite3 = require("sqlite3").verbose();
const path = require("path");
const app = express();
const PORT = process.env.PORT || 3000;
const dbPath = path.join(__dirname, "BancoDeDados.db");
const fs = require("fs");

const cors = require("cors");
app.use(cors());

if (!fs.existsSync(dbPath)) {
  console.error(
    "⚠️ ERRO: O arquivo do banco de dados 'BancoDeDados.db' não foi encontrado!"
  );
  process.exit(1);
}

const db = new sqlite3.Database(dbPath, (err) => {
  if (err) {
    console.error("❌ ERRO AO CONECTAR AO BANCO DE DADOS:", err.message);
  } else {
    console.log("✅ BANCO DE DADOS CONECTADO COM SUCESSO");
  }
});

app.use(express.json());

app.get("/", (req, res) => {
  res.send("🚀 Servidor rodando corretamente");
});

app.get("/motorista", (req, res) => {
  db.all(`SELECT * FROM Motorista`, [], (err, rows) => {
    if (err) {
      res.status(500).json({ error: err.message });
    } else {
      res.json({ motoristas: rows });
    }
  });
});

app.get("/motorista/:id", (req, res) => {
  const { id } = req.params;

  db.get(`SELECT * FROM Motorista WHERE id=?`, [id], (err, row) => {
    if (err) {
      res.status(500).json({ error: err.message });
    } else if (!row) {
      res.status(404).json({ message: "Motorista não encontrado!" });
    } else {
      res.json({ motoristas: row });
    }
  });
});

app.post("/motorista", (req, res) => {
  const {
    nome,
    sobrenome,
    telefone,
    email,
    senha,
    modelo_do_carro,
    placa_do_carro,
    frase_de_seguranca_1,
    frase_de_seguranca_2,
    frase_de_seguranca_3,
  } = req.body;

  if (
    !nome ||
    !sobrenome ||
    !telefone ||
    !email ||
    !senha ||
    !modelo_do_carro ||
    !placa_do_carro ||
    !frase_de_seguranca_1 ||
    !frase_de_seguranca_2 ||
    !frase_de_seguranca_3
  ) {
    return res.status(400).json({ error: "Todos os campos são obrigatórios!" });
  }

  // Query para inserir os dados
  const sql = `INSERT INTO Motorista 
  (
      nome, 
      sobrenome, 
      telefone, 
      email, 
      senha,
      modelo_do_carro, 
      placa_do_carro,
      frase_de_seguranca_1, 
      frase_de_seguranca_2, 
      frase_de_seguranca_3
  ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)`;

  const params = [
    nome,
    sobrenome,
    telefone,
    email,
    senha,
    modelo_do_carro,
    placa_do_carro,
    frase_de_seguranca_1,
    frase_de_seguranca_2,
    frase_de_seguranca_3,
  ];

  db.run(sql, params, function (err) {
    if (err) {
      return res.status(500).json({ error: err.message });
    }

    res.status(201).json({
      message: "Motorista cadastrado com sucesso!",
      motorista: {
        id: this.lastID,
        nome,
        sobrenome,
        telefone,
        email,
        modelo_do_carro,
        placa_do_carro,
        frase_de_seguranca_1,
        frase_de_seguranca_2,
        frase_de_seguranca_3,
      },
    });
  });
});

app.get("/passageiro", (req, res) => {
  db.all(`SELECT * FROM Passageiro`, [], (err, rows) => {
    if (err) {
      res.status(500).json({ error: err.message });
    } else {
      res.json({ passageiro: rows });
    }
  });
});

app.get("/passageiro/:id", (req, res) => {
  const { id } = req.params;

  db.get(`SELECT * FROM Passageiro WHERE id=?`, [id], (err, row) => {
    if (err) {
      res.status(500).json({ error: err.message });
    } else if (!row) {
      res.status(404).json({ message: "Passageiro não encontrado!" });
    } else {
      res.json({ passageiro: row });
    }
  });
});

app.post("/passageiro", (req, res) => {
  const { nome, sobrenome, telefone, email, senha } = req.body;

  if (!nome || !sobrenome || !telefone || !email || !senha) {
    return res.status(400).json({ error: "Todos os campos são obrigatórios!" });
  }

  const sql = `INSERT INTO Passageiro 
    (
      nome, 
      sobrenome, 
      telefone, 
      email, 
      senha
    ) VALUES (?, ?, ?, ?, ?)`;

  const params = [nome, sobrenome, telefone, email, senha];

  db.run(sql, params, function (err) {
    if (err) {
      if (err.message.includes("UNIQUE constraint failed")) {
        return res
          .status(409)
          .json({ error: "Telefone ou email já cadastrado!" });
      }
      return res.status(500).json({ error: err.message });
    }

    res.status(201).json({
      message: "Passageiro cadastrado com sucesso!",
      passageiro: {
        id: this.lastID,
        nome,
        sobrenome,
        telefone,
        email,
      },
    });
  });
});

app.get("/viagem", (req, res) => {
  db.all(`SELECT * FROM Viagem`, [], (err, rows) => {
    if (err) {
      res.status(500).json({ error: err.message });
    } else {
      res.json({ viagem: rows });
    }
  });
});

app.get("/viagem/:id", (req, res) => {
  const { id } = req.params;

  db.get(`SELECT * FROM Viagem WHERE id=?`, [id], (err, row) => {
    if (err) {
      res.status(500).json({ error: err.message });
    } else if (!row) {
      res.status(404).json({ message: "Viagem não encontrada!" });
    } else {
      res.json({ viagem: row });
    }
  });
});

app.get("/api/hello", (req, res) => {
  res.json({ message: "Oi do CodeSandbox!" });
});

app.listen(PORT, "0.0.0.0", () => {
  console.log(`🚀 Servidor rodando em ${PORT}`);
});
