<?php
class Rest extends MX_Controller{
function __construct() {
parent::__construct();
//is_logged_in();
	
}
	function index(){
		$tolak = json_encode("access denied");
		echo $tolak;
	}


	function user(){
			
		$user = $this->db->select('*')->from('user')->where('HWID', $this->uri->segment(3))->limit(1)->order_by('id', 'DESC')->get()->result(); //Untuk mengambil data dari database webinar
		$baca_sensor = $this->db->select('*')->from('datasensor')->where('HWID', $this->uri->segment(3))->limit(1)->order_by('id', 'DESC')->get()->result();	
			$response = array("user" => array());
				foreach ($user as $r) foreach ($baca_sensor as $b) {
				$temp = array(
				"nama" => $r->nama,
				"email" => $r->email,
				"token" => $r->token,
				"HWID" => $r->HWID,
				"waktu_penyemprotan" => $r->waktu_penyemprotan,
				"mode_penyemprotan" => $r->mode_penyemprotan,
				"password" => $r->password,

				"time" => $b->time,
				"baterai" => $b->baterai,
				"tangki_pestisida" => $b->tangki_pestisida,
				"pergerakan_robot" => $b->pergerakan_robot,
				"deteksi_hama" => $b->deteksi_hama,
				"jarak_hama" => $b->jarak_hama,
				"penyemprotan_hama" => $b->penyemprotan_hama				
			);
				
				array_push($response["user"], $temp);
			}
			$data = json_encode($response);
			echo "$data";
    }


	public function buatuser(){
    $nama = isset($_GET['nama']) ? $_GET['nama'] : '';
    $email = isset($_GET['email']) ? $_GET['email'] : '';
    $HWID = isset($_GET['HWID']) ? $_GET['HWID'] : '';
    $password = isset($_GET['password']) ? $_GET['password'] : '';

    // Periksa apakah email dan HWID sudah ada dalam database
    $this->db->where('email', $email);
    $this->db->or_where('HWID', $HWID);
    $existingUser = $this->db->get('user')->row();

    if ($existingUser) {
        echo "HWID atau Email Sudah Terdaftar"; // Pengiriman gagal jika email atau HWID sudah ada
    } else {
        $randomBytes = random_bytes(16); // Menghasilkan 16 byte acak
        $token = bin2hex($randomBytes); // Konversi ke format heksadesimal

        $isi = array(
            'nama'             => $nama,
            'email'            => $email,
            'HWID'             => $HWID,
            'password'         => password_hash($password, PASSWORD_DEFAULT),
            'token'            => $token,
            'waktu_penyemprotan'    => '00:00:00',
            'mode_penyemprotan'       => 'otomatis'
        );

        $this->db->insert('user', $isi);

        $isi2 = array(
            'email'            => $email,
            'HWID'             => $HWID,
			'baterai'     => 0,
			'tangki_pestisida'     => 0,
			'pergerakan_robot'     => "tidak",
			'deteksi_hama'     => "aman",
			'jarak_hama'     => 0,
			'penyemprotan_hama'     => "mati",
            'mode_penyemprotan'       => 'otomatis'

        );
		$this->db->insert('datasensor', $isi2);
        $this->db->trans_complete();
        if ($this->db->trans_status() === FALSE)
        {
            echo "gagal";
        } else {
            echo "sukses";
        }
    }
}
	
	function updateuser(){
	if($_SERVER['REQUEST_METHOD']=='POST'){
        $data = array(
            'nama'     => $this->input->post('nama'),
			'email'     => $this->input->post('email'),
			'token'     => $this->input->post('token'),
			'password' => password_hash($this->input->post('password'), PASSWORD_DEFAULT)

			
        );
        $hwid   = $this->input->post('HWID');
        $this->db->where('HWID',$hwid);
        $this->db->update('user',$data);
        
		$this->db->trans_complete();
		if ($this->db->trans_status() === FALSE)
		{
			echo "gagal";
		} else {
			echo "sukses";
		}
        }
	
    }


	function updateswaktupenyemprotan(){
		if($_SERVER['REQUEST_METHOD']=='POST'){
			$data = array(
				'waktu_penyemprotan'     => $this->input->post('waktu_penyemprotan')
				
			);
			$hwid   = $this->input->post('HWID');
			$this->db->where('HWID',$hwid);
			$this->db->update('user',$data);
			
			$this->db->trans_complete();
			if ($this->db->trans_status() === FALSE)
			{
				echo "gagal";
			} else {
				echo "sukses";
			}
			}
		
		}

		function updatemodepenyemprotan(){
			if($_SERVER['REQUEST_METHOD']=='POST'){
				$data = array(
					'mode_penyemprotan'     => $this->input->post('mode_penyemprotan')
					
				);
				$hwid   = $this->input->post('HWID');
				$this->db->where('HWID',$hwid);
				$this->db->update('user',$data);
				
				$this->db->trans_complete();
				if ($this->db->trans_status() === FALSE)
				{
					echo "gagal";
				} else {
					echo "sukses";
				}
				}
			
			}
	
	public function kirimdatasensor()
	{
	  $this->db->select('*')->from('user')->where('HWID', $_GET['HWID'])->where('token', $_GET['token'])->get()->row()->id;
	  $isi = array(
  
		'HWID'     => $_GET['HWID'],
		'email'     => $_GET['email'],
		'baterai'     => $_GET['baterai'],
		'tangki_pestisida'     => $_GET['tangki_pestisida'],
		'pergerakan_robot'     => $_GET['pergerakan_robot'],
		'deteksi_hama'     => $_GET['deteksi_hama'],
		'jarak_hama'     => $_GET['jarak_hama'],
		'penyemprotan_hama'     => $_GET['penyemprotan_hama'],
		'mode_penyemprotan'     => $_GET['mode_penyemprotan']

	  );
	  $this->db->insert('datasensor', $isi);
	  $this->db->trans_complete();
	  if ($this->db->trans_status() === FALSE)
	  {
		  echo "gagal";
	  } else {
		  echo "sukses";
	  }
	}

	function log_sensor(){
			
		$log_sensor = $this->db->select('*')->from('datasensor')->where('HWID', $this->uri->segment(3))->order_by('id', 'DESC')->get()->result(); //Untuk mengambil data dari database webinar
			$response = array("log_sensor" => array());
				foreach ($log_sensor as $r) {
				$temp = array(
				"time" => $r->time,
				"baterai" => $r->baterai,
				"tangki_pestisida" => $r->tangki_pestisida,
				"pergerakan_robot" => $r->pergerakan_robot,
				"deteksi_hama" => $r->deteksi_hama,
				"jarak_hama" => $r->jarak_hama,
				"penyemprotan_hama" => $r->penyemprotan_hama,
				"mode_penyemprotan" => $r->mode_penyemprotan
			);
				
				array_push($response["log_sensor"], $temp);
			}
			$data = json_encode($response);
			echo "$data";
    }

	
}