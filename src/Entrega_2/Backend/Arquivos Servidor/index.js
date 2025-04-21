const express = require("express");
const sqlite3 = require("sqlite3").verbose();
const path = require("path");
const fs = require("fs");
const cors = require("cors");

const app = express();
const PORT = process.env.PORT || 3000;
const dbPath = path.join(__dirname, "BancoDeDados.db");

app.use(cors());
app.use(express.json());

if (!fs.existsSync(dbPath)) {
  console.error(
    "⚠️ ERRO: O arquivo do banco de dados 'BancoDeDados.db' não foi encontrado!",
  );
  process.exit(1);
}

const db = new sqlite3.Database(dbPath, (err) => {
  if (err) console.error("❌ ERRO AO CONECTAR AO BANCO DE DADOS:", err.message);
  else console.log("✅ BANCO DE DADOS CONECTADO COM SUCESSO");
});

app.get("/", (req, res) => res.send("🚀 Servidor rodando corretamente"));

// --------------------- MOTORISTA ---------------------
//Lista todos os motoristas
app.get("/motorista", (req, res) => {
  db.all(`SELECT * FROM Motorista`, [], (err, rows) => {
    if (err) res.status(500).json({ error: err.message });
    else res.json({ motoristas: rows });
  });
});

//Encontra um motorista pelo ID
app.get("/motorista/:id", (req, res) => {
  db.get(`SELECT * FROM Motorista WHERE id=?`, [req.params.id], (err, row) => {
    if (err) res.status(500).json({ error: err.message });
    else if (!row)
      res.status(404).json({ message: "Motorista não encontrado!" });
    else res.json({ motoristas: row });
  });
});

//Adiciona um novo Motorista
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

  const sql = `
    INSERT INTO Motorista (nome, sobrenome, telefone, email, senha, modelo_do_carro, placa_do_carro, frase_de_seguranca_1, frase_de_seguranca_2, frase_de_seguranca_3)
    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
  `;
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
    if (err) return res.status(500).json({ error: err.message });
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

//Altera os dados de um Motorista
app.put("/motorista/:id", (req, res) => {
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

  const sql = `
    UPDATE Motorista SET nome=?, sobrenome=?, telefone=?, email=?, senha=?,
    modelo_do_carro=?, placa_do_carro=?, frase_de_seguranca_1=?, frase_de_seguranca_2=?, frase_de_seguranca_3=?
    WHERE id=?
  `;
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
    req.params.id,
  ];

  db.run(sql, params, function (err) {
    if (err) return res.status(500).json({ error: err.message });
    res.json({ message: "Motorista atualizado com sucesso!" });
  });
});

//Deleta um Motorista
app.delete("/motorista/:id", (req, res) => {
  db.run(`DELETE FROM Motorista WHERE id = ?`, [req.params.id], function (err) {
    if (err) return res.status(500).json({ error: err.message });
    res.json({ message: "Motorista deletado com sucesso!" });
  });
});

// --------------------- PASSAGEIRO ---------------------
//Lista todos os Passageiros
app.get("/passageiro", (req, res) => {
  db.all(`SELECT * FROM Passageiro`, [], (err, rows) => {
    if (err) res.status(500).json({ error: err.message });
    else res.json({ passageiro: rows });
  });
});

//Encontra um Passageiro pelo ID
app.get("/passageiro/:id", (req, res) => {
  db.get(`SELECT * FROM Passageiro WHERE id=?`, [req.params.id], (err, row) => {
    if (err) res.status(500).json({ error: err.message });
    else if (!row)
      res.status(404).json({ message: "Passageiro não encontrado!" });
    else res.json({ passageiro: row });
  });
});

//Adiciona um Passageiro
app.post("/passageiro", (req, res) => {
  const { nome, sobrenome, telefone, email, senha } = req.body;

  if (!nome || !sobrenome || !telefone || !email || !senha) {
    return res.status(400).json({ error: "Todos os campos são obrigatórios!" });
  }

  const sql = `INSERT INTO Passageiro (nome, sobrenome, telefone, email, senha) VALUES (?, ?, ?, ?, ?)`;
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
      passageiro: { id: this.lastID, nome, sobrenome, telefone, email },
    });
  });
});

//Altera os dados de um Passageiro
app.put("/passageiro/:id", (req, res) => {
  const { nome, sobrenome, telefone, email, senha } = req.body;

  const sql = `
    UPDATE Passageiro SET nome=?, sobrenome=?, telefone=?, email=?, senha=? WHERE id=?
  `;
  const params = [nome, sobrenome, telefone, email, senha, req.params.id];

  db.run(sql, params, function (err) {
    if (err) return res.status(500).json({ error: err.message });
    res.json({ message: "Passageiro atualizado com sucesso!" });
  });
});

//Deleta um Passageiro
app.delete("/passageiro/:id", (req, res) => {
  db.run(
    `DELETE FROM Passageiro WHERE id = ?`,
    [req.params.id],
    function (err) {
      if (err) return res.status(500).json({ error: err.message });
      res.json({ message: "Passageiro deletado com sucesso!" });
    },
  );
});

// --------------------- VIAGEM ---------------------
//Lista todas as Viagens
app.get("/viagem", (req, res) => {
  db.all(`SELECT * FROM Viagem`, [], (err, rows) => {
    if (err) res.status(500).json({ error: err.message });
    else res.json({ viagem: rows });
  });
});

//Encontra uma Viagem pelo ID
app.get("/viagem/:id", (req, res) => {
  db.get(`SELECT * FROM Viagem WHERE id=?`, [req.params.id], (err, row) => {
    if (err) res.status(500).json({ error: err.message });
    else if (!row) res.status(404).json({ message: "Viagem não encontrada!" });
    else res.json({ viagem: row });
  });
});

//Adiciona uma Viagem
app.post("/viagem", (req, res) => {
  const {
    endereco_de_partida,
    endereco_de_chegada,
    data_hora_de_partida,
    data_hora_de_chegada,
    status,
    motorista_id,
    passageiro_id,
  } = req.body;

  const sql = `
    INSERT INTO Viagem (endereco_de_partida, endereco_de_chegada, data_hora_de_partida, data_hora_de_chegada, status, motorista_id, passageiro_id)
    VALUES (?, ?, ?, ?, ?, ?, ?)
  `;
  const params = [
    endereco_de_partida,
    endereco_de_chegada,
    data_hora_de_partida,
    data_hora_de_chegada,
    status,
    motorista_id,
    passageiro_id,
  ];

  db.run(sql, params, function (err) {
    if (err) return res.status(500).json({ error: err.message });
    res
      .status(201)
      .json({ message: "Viagem cadastrada com sucesso!", id: this.lastID });
  });
});

//Altera os dados de uma viagem
app.put("/viagem/:id", (req, res) => {
  const {
    endereco_de_partida,
    endereco_de_chegada,
    data_hora_de_partida,
    data_hora_de_chegada,
    status,
    motorista_id,
    passageiro_id,
  } = req.body;

  const sql = `
    UPDATE Viagem SET endereco_de_partida=?, endereco_de_chegada=?, data_hora_de_partida=?, data_hora_de_chegada=?, status=?, motorista_id=?, passageiro_id=?
    WHERE id=?
  `;
  const params = [
    endereco_de_partida,
    endereco_de_chegada,
    data_hora_de_partida,
    data_hora_de_chegada,
    status,
    motorista_id,
    passageiro_id,
    req.params.id,
  ];

  db.run(sql, params, function (err) {
    if (err) return res.status(500).json({ error: err.message });
    res.json({ message: "Viagem atualizada com sucesso!" });
  });
});

//Deleta uma Viagem
app.delete("/viagem/:id", (req, res) => {
  db.run(`DELETE FROM Viagem WHERE id = ?`, [req.params.id], function (err) {
    if (err) return res.status(500).json({ error: err.message });
    res.json({ message: "Viagem deletada com sucesso!" });
  });
});

//Lista todas as viagens de um Passageiro
app.get("/viagem/passageiro/:id", (req, res) => {
  const passageiroId = req.params.id;
  const sql = `SELECT * FROM Viagem WHERE passageiro_id = ?`;

  db.all(sql, [passageiroId], (err, rows) => {
    if (err) return res.status(500).json({ error: err.message });
    res.json({ viagens: rows });
  });
});

//Lista todas as viagens de um Motorista
app.get("/viagem/motorista/:id", (req, res) => {
  const motoristaId = req.params.id;
  const sql = `SELECT * FROM Viagem WHERE motorista_id = ?`;

  db.all(sql, [motoristaId], (err, rows) => {
    if (err) return res.status(500).json({ error: err.message });
    res.json({ viagens: rows });
  });
});

app.listen(PORT, "0.0.0.0", () => {
  console.log(`🚀 Servidor rodando em ${PORT}`);
});
