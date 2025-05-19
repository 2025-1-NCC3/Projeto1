const express = require("express");
const router = express.Router();
const sqlite3 = require("sqlite3").verbose();
const path = require("path");

// Caminho do banco
const dbPath = path.join(__dirname, "../BancoDeDados.db");
const db = new sqlite3.Database(dbPath);

// LOGIN MOTORISTA
router.post("/motorista", (req, res) => {
  console.log("REQ.BODY MOTORISTA:", req.body); // 👀 Log do que foi recebido

  const { email, senha } = req.body;

  if (typeof email !== "string" || typeof senha !== "string") {
    console.log("❌ Email ou senha ausentes ou inválidos.");
    return res.status(400).json({ error: "Email e senha são obrigatórios!" });
  }

  db.get(`SELECT * FROM Motorista WHERE email = ?`, [email], (err, row) => {
    if (err) {
      console.error("❌ Erro no SELECT:", err.message);
      return res.status(500).json({ error: err.message });
    }

    if (!row) {
      console.warn("❌ Motorista não encontrado:", email);
      return res.status(404).json({ message: "Motorista não encontrado!" });
    }

    if (row.senha !== senha) {
      console.warn("❌ Senha incorreta:", senha);
      return res.status(401).json({ message: "Senha incorreta!" });
    }

    console.log("✅ Login MOTORISTA OK:", row.email);
    res.json({
      message: "Login realizado com sucesso!",
      tipo: "motorista",
      usuario: {
        id: row.id,
        nome: row.nome,
        email: row.email,
      },
    });
  });
});

// LOGIN PASSAGEIRO
router.post("/passageiro", (req, res) => {
  console.log("REQ.BODY PASSAGEIRO:", req.body); // 👀

  const { email, senha } = req.body;

  if (typeof email !== "string" || typeof senha !== "string") {
    console.log("❌ Email ou senha ausentes ou inválidos.");
    return res.status(400).json({ error: "Email e senha são obrigatórios!" });
  }

  db.get(`SELECT * FROM Passageiro WHERE email = ?`, [email], (err, row) => {
    if (err) {
      console.error("❌ Erro no SELECT:", err.message);
      return res.status(500).json({ error: err.message });
    }

    if (!row) {
      console.warn("❌ Passageiro não encontrado:", email);
      return res.status(404).json({ message: "Passageiro não encontrado!" });
    }

    if (row.senha !== senha) {
      console.warn("❌ Senha incorreta:", senha);
      return res.status(401).json({ message: "Senha incorreta!" });
    }

    console.log("✅ Login PASSAGEIRO OK:", row.email);
    res.json({
      message: "Login realizado com sucesso!",
      tipo: "passageiro",
      usuario: {
        id: row.id,
        nome: row.nome,
        email: row.email,
      },
    });
  });
});

module.exports = router;
