class HomeController < ApplicationController
  layout false


  def index

    token = params[:token]

    title = '测试 title'
    desc = '测试 desc'
    other = '测试 other43'

    
    p token

    message_json = {
      :has_unread => true,
      :messages => [
        {:title => title, :desc => desc, :other => other},
        {:title => title, :desc => desc, :other => other}
      ]
    }
    a_message = {:token => token, :message_json => message_json}.to_s

    message_json = {:has_unread => false} if has_token_and_message?(a_message)

    render :json => message_json
  end


  def has_token_and_message?(a_message)
    filename = 'token_and_message'
    open(filename, 'a+') do |f|
      f.each_line do |ff|
        
        ff = ff.tr("\n","")

        p ff
        p a_message

        return true if a_message == ff
      end

      f.puts a_message
      false
    end
  end

end
